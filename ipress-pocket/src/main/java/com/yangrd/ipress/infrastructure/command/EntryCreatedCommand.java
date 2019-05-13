package com.yangrd.ipress.infrastructure.command;

import lombok.Data;

/**
 * EntryCreatedCommand
 *
 * @author yangrd
 * @date 2019/05/13
 */
@Data
public class EntryCreatedCommand {

    /**
     * id
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * html内容
     */
    private String htmlContent;

    /**
     * markdown内容
     */
    private String mdContent;
}