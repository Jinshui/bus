this["Bus"] = this["Bus"] || {};

this["Bus"]["admin/editRoute"] = Handlebars.template({"1":function(depth0,helpers,partials,data) {
  return "<div class=\"del_button\" id=\"btn_delete_route\">\n删除\n</div>\n";
  },"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
  var stack1, buffer = "<div data-role=\"content\" style=\"padding: 0\">\n<div class=\"content_header\" style=\"background-color: gray;\">\n<h3>路线添加</h3>\n";
  stack1 = helpers['if'].call(depth0, (depth0 != null ? depth0.edit : depth0), {"name":"if","hash":{},"fn":this.program(1, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer + "</div>\n<div data-role=\"fieldcontain\" style=\"padding-left: 10px; padding-right: 10px\">\n<label for=\"route_name\">路线名称：</label>\n<input type=\"text\" name=\"name\" id=\"route_name\" placeholder=\"请输入路线名称\">\n<label for=\"route_map\">路线地图(选填)：</label>\n<input type=\"file\" name=\"map\" id=\"route_map\">\n<label for=\"route_price\">票价:</label>\n<input type=\"number\" name=\"price\" id=\"route_price\" placeholder=\"人民币\">\n<label for=\"start_station\">起点:</label>\n<input type=\"text\" name=\"startStation\" id=\"start_station\" placeholder=\"起始站\">\n<label for=\"start_station_desc\">起点描述:</label>\n<textarea name=\"startStationDesc\" id=\"start_station_desc\" placeholder=\"起始地点详细描述\"></textarea>\n<label for=\"end_station\">终点:</label>\n<input type=\"text\" name=\"endStation\" id=\"end_station\" placeholder=\"多个终点以顿号分隔\">\n<label for=\"middle_stations\">途经站:</label>\n<input type=\"text\" name=\"middleStations\" id=\"middle_stations\" placeholder=\"多个途经站以顿号分隔\">\n</div>\n<div class=\"footer_btn\" id=\"btn_edit_route_next_step\" >\n下一步\n</div>\n</div>\n";
},"useData":true});



this["Bus"]["admin/editRouteStep2"] = Handlebars.template({"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
  return "<div data-role=\"content\" style=\"padding: 0\">\n<div class=\"content_header\" style=\"background-color: gray;\">\n<h3>添加发车时间</h3>\n</div>\n<form>\n<div class=\"content_header\">\n生效日期A1\n</div>\n<div data-role=\"fieldcontain\">\n<label for=\"start_date\">生效日期:</label>\n<input type=\"date\" name=\"startDate\" id=\"start_date\"  placeholder=\"生效日期\">\n<label for=\"end_date\">截止日期:</label>\n<input type=\"date\" name=\"endDate\" id=\"end_date\"  placeholder=\"截止日期\">\n<label for=\"start_times\">发车时间:</label>\n<textarea name=\"startTimes\" id=\"start_times\"  placeholder=\"格式:0720 0740 中间用空格分开\"></textarea>\n<label for=\"weekend_on\">周末是否发车:</label>\n<input type=\"checkbox\" data-role=\"flipswitch\" name=\"weekendOn\" id=\"weekend_on\" data-on-text=\"是\" data-off-text=\"否\"/>\n</div>\n</form>\n<div class=\"footer_btn\">\n<a href=\"\">下一步</a>\n</div>\n</div>\n";
  },"useData":true});



this["Bus"]["admin/routes"] = Handlebars.template({"1":function(depth0,helpers,partials,data) {
  var stack1, helper, functionType="function", helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression, buffer = "<div class=\"content_header\">\n"
    + escapeExpression(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"name","hash":{},"data":data}) : helper)))
    + "\n<div class=\"edit_button\" id=\"btn_edit_route\">\n编辑\n</div>\n</div>\n<div class=\"text_panel\">\n<b>起点:</b>&nbsp;"
    + escapeExpression(((helper = (helper = helpers.startStation || (depth0 != null ? depth0.startStation : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"startStation","hash":{},"data":data}) : helper)))
    + "&nbsp;&nbsp;&nbsp;&nbsp;<b>终点:</b>&nbsp;"
    + escapeExpression(((helper = (helper = helpers.endStation || (depth0 != null ? depth0.endStation : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"endStation","hash":{},"data":data}) : helper)))
    + "<br>\n<b>途经:</b>&nbsp;";
  stack1 = helpers.each.call(depth0, (depth0 != null ? depth0.middleStations : depth0), {"name":"each","hash":{},"fn":this.program(2, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += "\n</div>\n";
  stack1 = helpers.each.call(depth0, (depth0 != null ? depth0.timeRanges : depth0), {"name":"each","hash":{},"fn":this.program(4, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"2":function(depth0,helpers,partials,data) {
  var lambda=this.lambda, escapeExpression=this.escapeExpression;
  return escapeExpression(lambda(depth0, depth0))
    + ", ";
},"4":function(depth0,helpers,partials,data) {
  var stack1, helper, functionType="function", helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression, buffer = "<div class=\"route_date_info\">日期: "
    + escapeExpression(((helper = (helper = helpers.startTime || (depth0 != null ? depth0.startTime : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"startTime","hash":{},"data":data}) : helper)))
    + " ~ "
    + escapeExpression(((helper = (helper = helpers.endTime || (depth0 != null ? depth0.endTime : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"endTime","hash":{},"data":data}) : helper)))
    + "</div>\n";
  stack1 = helpers['if'].call(depth0, (depth0 != null ? depth0.vehicles : depth0), {"name":"if","hash":{},"fn":this.program(5, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"5":function(depth0,helpers,partials,data) {
  var stack1, buffer = "<div class=\"text_panel\">\n";
  stack1 = helpers.each.call(depth0, (depth0 != null ? depth0.vehicles : depth0), {"name":"each","hash":{},"fn":this.program(6, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer + "</div>\n";
},"6":function(depth0,helpers,partials,data) {
  var stack1, helper, functionType="function", helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression, buffer = "<b>"
    + escapeExpression(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"name","hash":{},"data":data}) : helper)))
    + ":</b><br>\n"
    + escapeExpression(((helper = (helper = helpers.licenseTag || (depth0 != null ? depth0.licenseTag : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"licenseTag","hash":{},"data":data}) : helper)))
    + "&nbsp;"
    + escapeExpression(((helper = (helper = helpers.driverName || (depth0 != null ? depth0.driverName : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"driverName","hash":{},"data":data}) : helper)))
    + "&nbsp;"
    + escapeExpression(((helper = (helper = helpers.driverContact || (depth0 != null ? depth0.driverContact : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"driverContact","hash":{},"data":data}) : helper)))
    + "<br>\n发车时间: ";
  stack1 = helpers.each.call(depth0, (depth0 != null ? depth0.startPoints : depth0), {"name":"each","hash":{},"fn":this.program(2, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer + "\n<hr>\n";
},"8":function(depth0,helpers,partials,data) {
  return "<div style=\"width: 100%; text-align: center; font-size: x-large;\">\n<b>您尚未添加任何路线!</b>\n</div>\n";
  },"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
  var stack1, buffer = "<div data-role=\"header\" data-position=\"fixed\">\n<div class=\"content_header\" style=\"background-color: gray;\">\n<h3>路线管理</h3>\n</div>\n</div>\n<div data-role=\"content\" style=\"padding: 0\">\n";
  stack1 = helpers.each.call(depth0, (depth0 != null ? depth0.routes : depth0), {"name":"each","hash":{},"fn":this.program(1, data),"inverse":this.program(8, data),"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer + "</div>\n<div data-role=\"footer\" data-position=\"fixed\">\n<div class=\"footer_btn\" id=\"btn_add_route\">\n添加路线\n</div>\n</div>\n";
},"useData":true});