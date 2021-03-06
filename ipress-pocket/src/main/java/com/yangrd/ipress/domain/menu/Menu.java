package com.yangrd.ipress.domain.menu;

import com.yangrd.ipress.infrastructure.mode.AbstractModeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Menu
 *
 * @author yangrd
 * @date 2019/05/13
 */
@Getter
@Setter
@Accessors(chain = true)

@Entity
@Table(name = "p_menu")
@EntityListeners(AuditingEntityListener.class)
public class Menu extends AbstractModeEntity<Menu> {

    private String pocketId;

    private String name;

    private Integer sort;

    @Enumerated(EnumType.STRING)
    private MenuType type;

    private String parentId;

    public enum MenuType{
        /**
         * 菜单
         */
        FOLDER,
        /**
         * 文件
         */
        ENTRY;
    }
}
