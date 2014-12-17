package com.bus.services.model.weixin;

import java.util.ArrayList;
import java.util.List;

public class WeixinMenus {
    private static final String MENU_KEY_1 = "ADD_RESERVATION";
    private static final String MENU_KEY_2 = "MY_RESERVATIONS";
    private static final String MENU_KEY_3_1 = "REQUEST_NEW_ROUTE";
    private static final String MENU_KEY_3_2 = "ANNOUNCEMENTS";
    private static final String MENU_KEY_3_3 = "CONTACT_US";
    private List<MenuItem> button;
    public static class MenuItem{
        private String type;
        private String name;
        private String key;
        private String url;
        private List<MenuItem> sub_button;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<MenuItem> getSub_button() {
            if(sub_button == null){
                sub_button = new ArrayList<>();
            }
            return sub_button;
        }

        public void setSub_button(List<MenuItem> sub_button) {
            this.sub_button = sub_button;
        }
    }
    public List<MenuItem> getButton() {
        if(button == null){
            button = new ArrayList<>();
        }
        return button;
    }

    public void setButton(List<MenuItem> button) {
        this.button = button;
    }

    public static WeixinMenus getDefault(){
        WeixinMenus menus = new WeixinMenus();
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setType("click");
        menuItem1.setKey(MENU_KEY_1);
        menuItem1.setName("预约巴士");
        menus.getButton().add(menuItem1);

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setType("click");
        menuItem2.setKey(MENU_KEY_2);
        menuItem2.setName("我的预约");
        menus.getButton().add(menuItem2);

        MenuItem menuItem3 = new MenuItem();
        menuItem3.setName("其它");
        menus.getButton().add(menuItem3);

        MenuItem subMenuItem1 = new MenuItem();
        subMenuItem1.setType("click");
        subMenuItem1.setKey(MENU_KEY_3_1);
        subMenuItem1.setName("线路申请");
        menuItem3.getSub_button().add(subMenuItem1);

        MenuItem subMenuItem2 = new MenuItem();
        subMenuItem2.setType("click");
        subMenuItem2.setKey(MENU_KEY_3_2);
        subMenuItem2.setName("巴士公告");
        menuItem3.getSub_button().add(subMenuItem2);


        MenuItem subMenuItem3 = new MenuItem();
        subMenuItem3.setType("click");
        subMenuItem3.setKey(MENU_KEY_3_3);
        subMenuItem3.setName("其它");
        menuItem3.getSub_button().add(subMenuItem3);
        return menus;
    }
}
