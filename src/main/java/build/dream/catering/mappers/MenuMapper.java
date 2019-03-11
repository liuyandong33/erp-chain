package build.dream.catering.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface MenuMapper {
    long insertAllMenuBranchR(@Param("menuId") BigInteger menuId,
                              @Param("tenantId") BigInteger tenantId,
                              @Param("tenantCode") String tenantCode,
                              @Param("branchIds") List<BigInteger> branchIds);

    long deleteAllMenuBranchR(@Param("menuId") BigInteger menuId, @Param("tenantId") BigInteger tenantId);
}
