package org.ssafy.load.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssafy.load.application.AddressService;
import org.ssafy.load.application.GoodsService;
import org.ssafy.load.common.dto.Response;
import org.ssafy.load.dto.Address;
import org.ssafy.load.dto.Goods;
import org.ssafy.load.dto.response.BuildingResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/simulation")
public class SimulationController {

    public final GoodsService goodsService;
    public final AddressService addressService;

    @GetMapping("/goods")
    public Response<List<Goods>> getGoods(){
        return Response.success(goodsService.getGoods());
    }

    @GetMapping
    public Response<List<BuildingResponse>> getBuildingJuso(){
        return Response.success(addressService.getBuildingJuso());
    }

}
