package build.dream.erp.mappers;

import build.dream.common.erp.domains.ElemeOrderStateChangeMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ElemeOrderStateChangeMessageMapper {
    long insert(ElemeOrderStateChangeMessage elemeOrderStateChangeMessage);
}