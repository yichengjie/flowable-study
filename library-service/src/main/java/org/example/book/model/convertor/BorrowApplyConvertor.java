package org.example.book.model.convertor;

import org.example.book.entity.BorrowApply;
import org.example.book.model.dto.BorrowApplyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BorrowApplyConvertor {

    BorrowApplyConvertor I = Mappers.getMapper(BorrowApplyConvertor.class);

    BorrowApplyDTO toDTO(BorrowApply borrowApply);
}
