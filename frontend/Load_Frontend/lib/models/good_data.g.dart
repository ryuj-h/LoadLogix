// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'good_data.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

GoodsData _$GoodsDataFromJson(Map<String, dynamic> json) => GoodsData(
      goodsId: (json['goodsId'] as num).toInt(),
      type: json['type'] as String,
      position: Vector3.fromJson(json['position'] as Map<String, dynamic>),
      weight: (json['weight'] as num).toInt(),
      buildingId: (json['buildingId'] as num).toInt(),
      buildingName: json['buildingName'] as String,
      detailAddress: json['detailAddress'] as String,
    );

Map<String, dynamic> _$GoodsDataToJson(GoodsData instance) => <String, dynamic>{
      'goodsId': instance.goodsId,
      'type': instance.type,
      'position': instance.position,
      'weight': instance.weight,
      'buildingId': instance.buildingId,
      'buildingName': instance.buildingName,
      'detailAddress': instance.detailAddress,
    };
