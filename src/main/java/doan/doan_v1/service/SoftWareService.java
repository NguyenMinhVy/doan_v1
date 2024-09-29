package doan.doan_v1.service;

import doan.doan_v1.dto.SoftWareDto;

import java.util.List;

public interface SoftWareService {

//    SoftWareDto getSoftWareDtoById(int id);
//
//    List<Integer> getSoftWareIdListByComputerId(int computerId);
//
//    List<SoftWareDto> getSoftWareDtoListByIdList(List<Integer> idList);

    List<SoftWareDto> getSoftWareDtoListByComputerId(int computerId);

    SoftWareDto getSoftWareDtoById(int softWareId);


}
