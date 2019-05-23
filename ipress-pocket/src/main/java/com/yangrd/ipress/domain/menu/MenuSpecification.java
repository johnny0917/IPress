package com.yangrd.ipress.domain.menu;

import com.github.wenhao.jpa.Specifications;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

/**
 * MenuSpecification
 *
 * @author yangrd
 * @date 2019/05/18
 */
public class MenuSpecification {

    public static Specification<Menu> toSpec(Menu.MenuType menuType, String username, String pocketId){
        return Specifications.<Menu>and().eq(Objects.nonNull(menuType),"type",menuType).eq(Objects.nonNull(username),"mode.userName",username).eq(Objects.nonNull(pocketId),"pocketId", pocketId).build();
    }
}
