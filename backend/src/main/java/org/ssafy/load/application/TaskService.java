package org.ssafy.load.application;

import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssafy.load.common.dto.ErrorCode;
import org.ssafy.load.common.exception.CommonException;
import org.ssafy.load.dao.BuildingRepository;
import org.ssafy.load.dao.GoodsRepository;
import org.ssafy.load.dao.LoadTaskRepository;
import org.ssafy.load.domain.BuildingEntity;
import org.ssafy.load.domain.CarEntity;
import org.ssafy.load.domain.GoodsEntity;
import org.ssafy.load.domain.LoadTaskEntity;
import org.ssafy.load.dto.response.LoadTaskCarResponse;
import org.ssafy.load.dto.response.LoadTaskGoodsResponse;
import org.ssafy.load.dto.response.LoadTaskResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final PathService pathService;
    private final LoadTaskRepository loadTaskRepository;
    private final BuildingRepository buildingRepository;
    private final GoodsRepository goodsRepository;

    @Transactional
    public LoadTaskResponse getLoadingTask() {
        Optional<LoadTaskEntity> loadTaskEntityOptional = loadTaskRepository.findFirstByAreaStatusIsTrueAndCompleteIsFalseOrderByCreatedAt();

        if(loadTaskEntityOptional.isEmpty()) {
            return null;
        }

        LoadTaskEntity loadTaskEntity = loadTaskEntityOptional.get();
        CarEntity carEntity = loadTaskEntity.getArea().getWorker().getCar();
        List<GoodsEntity> goodsEntityList = goodsRepository.findByLoadTask(loadTaskEntity);

        //Hard Coding (refactoring require!)
        //get start point building (삼성 연수원)
        Optional<BuildingEntity> SSAFYBuildingOptional = buildingRepository.findByZibunMainAndZibunSubAndArea(124, 0, loadTaskEntity.getArea());
        BuildingEntity SSAFYBuildingEntity = SSAFYBuildingOptional.orElseThrow(() -> new CommonException(ErrorCode.INTERNAL_SERVER_ERROR, "start point is not exist"));

        List<BuildingEntity> buildingEntityList = buildingRepository.findByLoadTask(loadTaskEntityOptional.get());
        buildingEntityList.addFirst(SSAFYBuildingEntity);
        buildingEntityList = pathService.calPathOrder(buildingEntityList);

        //응답 생성
        LoadTaskCarResponse car = new LoadTaskCarResponse(
                carEntity.getWidth(),
                carEntity.getHeight(),
                carEntity.getLength(),
                carEntity.getMaxPayload());

        List<LoadTaskGoodsResponse> goods = new ArrayList<>();
        for(GoodsEntity goodsEntity : goodsEntityList) {
            goods.add(new LoadTaskGoodsResponse(
                    goodsEntity.getId(),
                    goodsEntity.getBoxType().getType().toString(),
                    goodsEntity.getWeight(),
                    goodsEntity.getBuilding().getId())
            );
        }

        List<Long> buildingIdOrder = new ArrayList<>();
        for(BuildingEntity buildingEntity : buildingEntityList) {
            buildingIdOrder.add(buildingEntity.getId());
        }

        return new LoadTaskResponse(
                loadTaskEntity.getId(),
                car,
                goods,
                buildingIdOrder
        );
    }
}
