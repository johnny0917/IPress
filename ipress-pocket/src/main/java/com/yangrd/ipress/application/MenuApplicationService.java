package com.yangrd.ipress.application;

import com.yangrd.ipress.application.dto.FolderFlat;
import com.yangrd.ipress.application.dto.MenuTree;
import com.yangrd.ipress.domain.menu.Menu;
import com.yangrd.ipress.domain.menu.MenuFactory;
import com.yangrd.ipress.domain.menu.MenuRepository;
import com.yangrd.ipress.domain.menu.MenuSpecification;
import com.yangrd.ipress.infrastructure.SecurityUtils;
import com.yangrd.ipress.infrastructure.command.MenuCreatedCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MenuApplicationService
 *
 * @author yangrd
 * @date 2019/05/13
 */
@Service
public class MenuApplicationService extends AbstractModePermissionService<Menu, MenuCreatedCommand, MenuFactory, MenuRepository> {
    /**
     * 获取当前用户菜单树
     *
     * @return
     */
    public Set<MenuTree> getCurrentUserMenuTrees(String pocketId) {
        return buildMenuTree(repository.findAll(MenuSpecification.toSpec(null, SecurityUtils.getCurrentUsername(),pocketId)));
    }

    /**
     * 获取当前用户展平后的文件夹
     *
     * @return
     */
    public List<FolderFlat> listFolderFlatByCurrentUsername(String pocketId) {
        List<FolderFlat> list = new ArrayList<>();
        flat(listFolderTreeByCurrentUsername(pocketId), list, 0);
        return list;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    /**
     * 获取所有父元素的id
     *
     * @param id
     * @return
     */
    public List<String> listParentsId(String id) {
        List<String> parentIds = new ArrayList<>();
        listParentsId(parentIds, id);
        return parentIds;
    }

    private void listParentsId(List<String> ids, String id) {
        Optional<Menu> menuOptional = repository.findById(id);
        menuOptional.ifPresent(menu -> {
            if (menu.getParentId() != null) {
                ids.add(menu.getParentId());
                listParentsId(ids, menu.getParentId());
            }
        });
    }

    private Set<MenuTree> listFolderTreeByCurrentUsername(String pocketId) {
        return buildMenuTree(repository.findAll(MenuSpecification.toSpec(Menu.MenuType.FOLDER, SecurityUtils.getCurrentUsername(), pocketId)));
    }

    private void flat(Set<MenuTree> trees, List<FolderFlat> list, Integer level) {
        for (MenuTree tree : trees) {
            FolderFlat folderFlat = new FolderFlat(tree.getId(), prefix(level) + tree.getName(), tree.getName(), tree.getSort(), tree.getParentId(), tree.getCreatedTime());
            list.add(folderFlat);
            int level1 = level;
            if (!tree.getChildren().isEmpty()) {
                flat(tree.getChildren(), list, ++level1);
            }
        }
    }

    private String prefix(Integer level) {
        String prefix = "";
        for (int i = 0; i < level; i++) {
            prefix += "-";
        }
        return prefix;
    }


    private Set<MenuTree> buildMenuTree(List<Menu> menus) {
        Set<MenuTree> menuTrees = conversion2Set(menus);
        Map<String, MenuTree> menuTreeMap = menuTrees.stream().collect(Collectors.toMap(MenuTree::getId, Function.identity()));
        menuTrees.forEach(menuTree -> {
            if (menuTree.presentParent()) {
                menuTreeMap.get(menuTree.getParentId()).addChildren(menuTree);
            }
        });
        return new TreeSet<>(menuTrees.stream().filter(MenuTree::isTopMenu).collect(Collectors.toSet()));
    }

    private Set<MenuTree> conversion2Set(List<Menu> menus) {
        return new TreeSet<>(menus.stream().map(menu -> {
            MenuTree menuTree = new MenuTree();
            menuTree.setType(menu.getType().ordinal());
            menuTree.setCreatedTime(menu.getCreatedTime());
            BeanUtils.copyProperties(menu, menuTree);
            return menuTree;
        }).collect(Collectors.toSet()));
    }
}
