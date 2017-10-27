package com.jadlsoft.taglib.page;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 显示导航条,可利用此改变每页的记录数,上下翻页和跳页
 * 使用方法:<page:navigator type='BUTTON'/>
 * @author Starboy
 * @version 2.0
 */
public class NavigatorTag extends TagSupport {
    /**导航条的类型(BUTTON/TEXT)(按钮型/文字链接型)*/
    private String type = "BUTTON"; //选择导航条类型默认"BUTTON"(BUTTON/TEXT)

    public void setType(String newType) {
        type = newType;
    }

    public int doStartTag() throws JspException {
        try {
            String bar = getNavigatorBar(type);
            pageContext.getOut().write(bar);
            return SKIP_BODY;
        }
        catch (IOException ioe) {
            throw new JspException(ioe.getMessage());
        }
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    /**
     * 根据指定类型获得导航条预先设计的导航条
     * @param type 导航条类型(BUTTON/TEXT)
     * @return 返回导航条的HTML代码,若指定类型不存在,返回""
     */
    private String getNavigatorBar(String type) {
        String bar = "";
        String pageNo   = ((Integer) pageContext.getAttribute("pageNo")).toString();
        String pages    = ((Integer) pageContext.getAttribute("pages")).toString();
        String total    = ((Integer) pageContext.getAttribute("total")).toString();
        String pageSize = ((Integer) pageContext.getAttribute("pageSize")).toString();
        String nextDisabled = "";
        String prevDisabled = "";
        if (Integer.parseInt(pageNo) >= Integer.parseInt(pages))
            nextDisabled = "disabled";
        if (Integer.parseInt(pageNo) <= 1)
            prevDisabled = "disabled";

        //---------------------按钮型的导航条-----------------------//
        if (type.equalsIgnoreCase("BUTTON")) {
            String pageSizeInput = "<input type='text' size='2' value='" + pageSize + "' "
                                   + "onChange=\"javascript:this.form.choice.value='current';"
                                   + "this.form.pageSize.value=this.value;this.form.submit();\">";
            String firstButton = "<input type='button' value='首  页' " + prevDisabled + " "
                                 +
                    "onClick=\"javascript:this.form.choice.value='first';this.form.submit();\">";
            String prevButton = "<input type='button' value='上一页' " + prevDisabled + " "
                                +
                    "onClick=\"javascript:this.form.choice.value='prev';this.form.submit();\">";
            String nextButton = "<input type='button' value='下一页' " + nextDisabled + " "
                                +
                    "onClick=\"javascript:this.form.choice.value='next';this.form.submit();\">";
            String lastButton = "<input type='button' value='最后一页' " + nextDisabled + " "
                                +
                    "onClick=\"javascript:this.form.choice.value='last';this.form.submit();\">";
            String pageNoInput = "<input type='text' size='3' value='" + pageNo + "' "
                                 + "onChange=\"javascript:this.form.pageNo.value=this.value\">";

            bar = "每页pageSize条记录 | \n"
                  + "共pages页/total条记录 | \n"
                  + "first \n prev \n next \n last \n | 第pageNo页\n"
                  +
                    " <input type='submit' value='go' onClick=\"javascript:this.form.choice.value='current';\">\n";

            bar = bar.replaceAll("pageSize", pageSizeInput);
            bar = bar.replaceAll("pages", pages);
            bar = bar.replaceAll("total", total);
            bar = bar.replaceAll("first", firstButton);
            bar = bar.replaceAll("prev", prevButton);
            bar = bar.replaceAll("next", nextButton);
            bar = bar.replaceAll("last", lastButton);
            bar = bar.replaceAll("pageNo", pageNoInput);
        } /////end of if(button)

        //-------------------------文字型----------------------------//
        if (type.equalsIgnoreCase("TEXT")) {
            String pageSizeInput = "<input type='text' size='2' maxlength='3' name='tmp1' value='" + pageSize + "' "
                                   + "onChange=\"document.pager.choice.value='current';"
                                   + "this.form.pageSize.value=this.value;this.form.submit();\">";
            String firstText = "首  页";
            String prevText = "上一页";
            String nextText = "下一页";
            String lastText = "最后一页";
            if (prevDisabled.equalsIgnoreCase("")) {
                firstText = "<a href='first' "
                            +
                        "onClick=\"javascript:document.pager.choice.value='first';document.pager.submit();return false;\">"
                            + "首  页"
                            + "</a>";
                prevText = "<a href='prev' "
                           +
                        "onClick=\"javascript:document.pager.choice.value='prev';document.pager.submit();return false;\">"
                           + "上一页"
                           + "</a>";
            }
            if (nextDisabled.equalsIgnoreCase("")) {
                nextText = "<a href='next' "
                           +
                        "onClick=\"javascript:document.pager.choice.value='next';document.pager.submit();return false;\">"
                           + "下一页"
                           + "</a>";
                lastText = "<a href='last' "
                           +
                        "onClick=\"javascript:document.pager.choice.value='last';document.pager.submit();return false;\">"
                           + "最后一页"
                           + "</a>";
            }
            String pageNoInput = "<input type='text' size='3' maxlength='4'  name='tmp2' value='" + pageNo + "' "
                                 + "onChange=\"javascript:this.form.pageNo.value=this.value\">";

            bar = "每页pageSize条记录 | \n"
                  + "共pages页/total条记录 | \n"
                  + "first \n prev \n next \n last \n | 第pageNo页\n"
                  +
                    " <input type='submit' class='Button_Go' value='go' onClick=\"javascript:this.form.tmp1.fireEvent('onchange');this.form.tmp2.fireEvent('onchange');this.form.choice.value='current';\">\n";

            bar = bar.replaceAll("pageSize", pageSizeInput);
            bar = bar.replaceAll("pages", pages);
            bar = bar.replaceAll("total", total);
            bar = bar.replaceAll("first", firstText);
            bar = bar.replaceAll("prev", prevText);
            bar = bar.replaceAll("next", nextText);
            bar = bar.replaceAll("last", lastText);
            bar = bar.replaceAll("pageNo", pageNoInput);
        } /////end of if(text)

        //---------------------按钮型的导航条-----------------------//
        if (type.equalsIgnoreCase("SIMPLEBUTTON")) {
            String pageSizeInput = "<input class='navbar' type='text' size='1' value='" + pageSize +
                                   "' "
                                   + "onChange=\"javascript:this.form.choice.value='current';"
                                   + "this.form.pageSize.value=this.value;this.form.submit();\">";
            String prevButton = "<input class='navbar' type='button' value='上一页' " + prevDisabled +
                                " "
                                +
                    "onClick=\"javascript:this.form.choice.value='prev';this.form.submit();\">";
            String nextButton = "<input class='navbar' type='button' value='下一页' " + nextDisabled +
                                " "
                                +
                    "onClick=\"javascript:this.form.choice.value='next';this.form.submit();\">";
            String pageNoInput = "<input class='navbar' type='text' size='1' value='" + pageNo +
                                 "' "
                                 + "onChange=\"javascript:this.form.pageNo.value=this.value\">";

            bar = "每页pageSize条记录 | \n"
                  + "共pages页/total条记录 | \n"
                  + "\n prev \n next \n | 第pageNo页\n"
                  + " <input class='navbar' type='submit' value='go' onClick=\"javascript:this.form.choice.value='current';\">\n";

            bar = bar.replaceAll("pageSize", pageSizeInput);
            bar = bar.replaceAll("pages", pages);
            bar = bar.replaceAll("total", total);
            bar = bar.replaceAll("prev", prevButton);
            bar = bar.replaceAll("next", nextButton);
            bar = bar.replaceAll("pageNo", pageNoInput);
        } /////end of if(button)
        
        //---------------------按钮型的导航条(只有上、下页)-----------------------//
        if (type.equalsIgnoreCase("SIMPLEBUTTON2")) {
            String pageSizeInput = "<input class='navbar' type='text' size='1' value='" + pageSize +
                                   "' "
                                   + "onChange=\"javascript:this.form.choice.value='current';"
                                   + "this.form.pageSize.value=this.value;this.form.submit();\">";
            String prevButton = "<input class='navbar' type='button' value='上一页' " + prevDisabled +
                                " "
                                +
                    "onClick=\"javascript:this.form.choice.value='prev';this.form.submit();\">";
            String nextButton = "<input class='navbar' type='button' value='下一页' " + nextDisabled +
                                " "
                                +
                    "onClick=\"javascript:this.form.choice.value='next';this.form.submit();\">";
            String pageNoInput = "<input class='navbar' type='text' size='1' value='" + pageNo +
                                 "' "
                                 + "onChange=\"javascript:this.form.pageNo.value=this.value\">";

            bar = "\n prev \n next \n";

            bar = bar.replaceAll("pageSize", pageSizeInput);
            bar = bar.replaceAll("pages", pages);
            bar = bar.replaceAll("total", total);
            bar = bar.replaceAll("prev", prevButton);
            bar = bar.replaceAll("next", nextButton);
            bar = bar.replaceAll("pageNo", pageNoInput);
        }

        //-------------------------文字型----------------------------//
        if (type.equalsIgnoreCase("SIMPLETEXT")) {
            String pageSizeInput = "<input type='text' size='1' value='" + pageSize + "' "
                                   + "onChange=\"javascript:document.pager.choice.value='current';"
                                   + "this.form.pageSize.value=this.value;this.form.submit();\">";
            String firstText = "首  页";
            String prevText = "上一页";
            String nextText = "下一页";
            String lastText = "最后一页";
            if (prevDisabled.equalsIgnoreCase("")) {
                firstText = "<a href='first' "
                            +
                        "onClick=\"javascript:document.pager.choice.value='first';document.pager.submit();return false;\">"
                            + "首  页"
                            + "</a>";
                prevText = "<a href='prev' "
                           +
                        "onClick=\"javascript:document.pager.choice.value='prev';document.pager.submit();return false;\">"
                           + "上一页"
                           + "</a>";
            }
            if (nextDisabled.equalsIgnoreCase("")) {
                nextText = "<a href='next' "
                           +
                        "onClick=\"javascript:document.pager.choice.value='next';document.pager.submit();return false;\">"
                           + "下一页"
                           + "</a>";
                lastText = "<a href='last' "
                           +
                        "onClick=\"javascript:document.pager.choice.value='last';document.pager.submit();return false;\">"
                           + "最后一页"
                           + "</a>";
            }
            String pageNoInput = "<input type='text' size='1' value='" + pageNo + "' "
                                 + "onChange=\"javascript:this.form.pageNo.value=this.value\">";

            bar = "每页pageSize条记录 | \n"
                  + "共pages页/total条记录 | \n"
                  + "prev \n next \n | 第pageNo页\n"
                  +
                    " <input type='submit' value='go' onClick=\"javascript:this.form.choice.value='current';\">\n";

            bar = bar.replaceAll("pageSize", pageSizeInput);
            bar = bar.replaceAll("pages", pages);
            bar = bar.replaceAll("total", total);
            bar = bar.replaceAll("prev", prevText);
            bar = bar.replaceAll("next", nextText);
            bar = bar.replaceAll("pageNo", pageNoInput);
        } /////end of if(text)
        return bar;
    }
}