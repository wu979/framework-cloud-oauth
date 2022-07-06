package com.framework.cloud.oauth.domain.convert;

import cn.hutool.core.collection.CollectionUtil;
import com.framework.cloud.common.utils.StringUtil;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.rpc.vo.TenantVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 租户转换器
 *
 * @author wusiwei
 */
@Mapper(componentModel = "spring")
public interface TenantConvert {

    @Mappings(value = {
            @Mapping(target = "clientId", source = "code"),
            @Mapping(target = "clientSecret", source = "secret"),
            @Mapping(target = "registeredRedirectUri", source = "redirectUri", qualifiedByName = "redirectUri"),
            @Mapping(target = "scope", source = "scope", qualifiedByName = "scope"),
            @Mapping(target = "resourceIds", source = "resourceIds", qualifiedByName = "resourceIds"),
            @Mapping(target = "authorizedGrantTypes", source = "grantType", qualifiedByName = "grantType"),
            @Mapping(target = "autoApproveScopes", source = "approve", qualifiedByName = "approve"),
            @Mapping(target = "accessTokenValiditySeconds", source = "accessTokenValidity"),
            @Mapping(target = "refreshTokenValiditySeconds", source = "refreshTokenValidity"),
            @Mapping(target = "authorities", source = "authorities", qualifiedByName = "authorities"),
    })
    BaseTenant infoToBase(TenantVO tenantVO);

    @Named("redirectUri")
    default Set<String> redirectUri(String redirectUri) {
        if (StringUtil.isNotBlank(redirectUri)) {
            return Stream.of(redirectUri.split(",")).collect(Collectors.toSet());
        }
        return CollectionUtil.newHashSet();
    }

    @Named("scope")
    default Set<String> scope(String scope) {
        if (StringUtil.isNotBlank(scope)) {
            return Stream.of(scope.split(",")).collect(Collectors.toSet());
        }
        return CollectionUtil.newHashSet();
    }

    @Named("resourceIds")
    default Set<String> resourceIds(String resourceIds) {
        if (StringUtil.isNotBlank(resourceIds)) {
            return Stream.of(resourceIds.split(",")).collect(Collectors.toSet());
        }
        return CollectionUtil.newHashSet();
    }

    @Named("grantType")
    default Set<String> grantType(String grantType) {
        if (StringUtil.isNotBlank(grantType)) {
            return Stream.of(grantType.split(",")).collect(Collectors.toSet());
        }
        return CollectionUtil.newHashSet();
    }

    @Named("approve")
    default Set<String> approve(String approve) {
        if (StringUtil.isNotBlank(approve)) {
            return Stream.of(approve.split(",")).collect(Collectors.toSet());
        }
        return CollectionUtil.newHashSet();
    }

    @Named("authorities")
    default List<GrantedAuthority> authorities(String authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (StringUtil.isNotBlank(authorities)) {
            grantedAuthorities = Stream.of(authorities.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return grantedAuthorities;
    }


}
