<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8" />
		<title>一键下单</title>
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
		<link   rel="icon" href="${basePath}/favicon.ico" type="image/x-icon" />
		<link   rel="shortcut icon" href="${basePath}/favicon.ico" />
		<link href="${basePath}/js/common/bootstrap/3.3.5/css/bootstrap.min.css?${_v}" rel="stylesheet"/>
		<link href="${basePath}/css/common/base.css?${_v}" rel="stylesheet"/>
		<script  src="${basePath}/js/common/jquery/jquery1.8.3.min.js"></script>
		<script  src="${basePath}/js/common/layer/layer.js"></script>
		<script  src="${basePath}/js/common/bootstrap/3.3.5/js/bootstrap.min.js"></script>
		<script  src="${basePath}/js/shiro.demo.js"></script>
		<script >
			so.init(function(){
				//初始化全选。
				so.checkBoxInit('#checkAll','[check=box]');
				so.checkBoxInit('#checkAll_accounts','[check=box_account]');
				// $('#checkAll_accounts').click();
				<@shiro.hasPermission name="/permission/clearPermissionByRoleIds.shtml">
				//全选
				so.id('deleteAll').on('click',function(){
					var checkeds = $('[check=box]:checked');
					if(!checkeds.length){
						return layer.msg('请选择要清除的角色。',so.default),!0;
					}
					var array = [];
					checkeds.each(function(){
						array.push(this.value);
					});
					return deleteById(array);
				});
				</@shiro.hasPermission>

				//取行情
				<@shiro.hasPermission name="/trade/ticker.shtml">
                $.post('${basePath}/trade/ticker.shtml',{symbol:'btc_usd'},function(result){
                    if(result && result.status == 200){
                        console.log('===200==='+result.message);
                        $('#trade_ticker').html(result.message.ticker.last);
                        $('#trade_curr_tm').html(result.message.curr_tm);

                    }else{
                        console.log('====500===')
                    }
                },'json');
				</@shiro.hasPermission>
			});

            //下单
			<@shiro.hasPermission name="/trade/ticker.shtml">
			function trd_post(trd_type){

                var trd_symbol = $('#trd_symbol').val(),
                trd_price  = $('#trd_price').val();
                trd_amount  = $('#trd_amount').val();
                if($.trim(trd_price) == ''){
                    return layer.msg('价格不能为空。');
                }
                if($.trim(trd_amount) == ''){
                    return layer.msg('数量不能为空。');
                }

                var trd_accounts = "";
                var checkeds = $('[check=box_account]:checked');
                console.log(checkeds.length);
                $.each(checkeds,function(){
                    //console.log(this.value);
                    trd_accounts = trd_accounts+(this.value+",")
                });

                <#--loding-->
                var load = layer.load();
                $.post('${basePath}/trade/trade.shtml',
						{trd_symbol:trd_symbol, trd_type:trd_type,trd_price:trd_price,trd_amount:trd_amount,trd_accounts:trd_accounts},
						function(result){
                    layer.close(load);
                    if(result && result.status != 200){
                        return layer.msg(result.message,so.default),!1;
                    }
                    layer.msg('下单成功。');
                    setTimeout(function(){
                        $('#formId').submit();
                    },1000);
                },'json');
            }
			</@shiro.hasPermission>


			<@shiro.hasPermission name="/permission/clearPermissionByRoleIds.shtml">
			<#--根据ID数组清空角色的权限-->
			function deleteById(ids){
				var index = layer.confirm("确定清除这"+ ids.length +"个角色的权限？",function(){
					var load = layer.load();
					$.post('${basePath}/permission/clearPermissionByRoleIds.shtml',{roleIds:ids.join(',')},function(result){
						layer.close(load);
						if(result && result.status != 200){
							return layer.msg(result.message,so.default),!0;
						}else{
							layer.msg(result.message);
							setTimeout(function(){
								$('#formId').submit();
							},1000);
						}
					},'json');
					layer.close(index);
				});
			}
			</@shiro.hasPermission>
			<@shiro.hasPermission name="/permission/addPermission2Role.shtml">
			<#--选择权限后保存-->
			function selectPermission(){
				var checked = $("#boxRoleForm  :checked");
				var ids=[],names=[];
				$.each(checked,function(){
					ids.push(this.id);
					names.push($.trim($(this).attr('name')));
				});
				var index = layer.confirm("确定操作？",function(){
					<#--loding-->
					var load = layer.load();
					$.post('${basePath}/permission/addPermission2Role.shtml',{ids:ids.join(','),roleId:$('#selectRoleId').val()},function(result){
						layer.close(load);
						if(result && result.status != 200){
							return layer.msg(result.message,so.default),!1;
						}
						layer.msg('添加成功。');
						setTimeout(function(){
							$('#formId').submit();
						},1000);
					},'json');
				});
			}
			/*
			*根据角色ID选择权限，分配权限操作。
			*/
			function selectPermissionById(id){
				var load = layer.load();
				$.post("${basePath}/permission/selectPermissionById.shtml",{id:id},function(result){
					layer.close(load);
					if(result && result.length){
						var html =[];
						html.push('<div class="checkbox"><label><input type="checkbox"  selectAllBox="">全选</label></div>');
						$.each(result,function(){
							html.push("<div class='checkbox'><label>");
							html.push("<input type='checkbox' selectBox='' id='");
							html.push(this.id);
							html.push("'");
							if(this.check){
								html.push(" checked='checked'");
							}
							html.push("name='");
							html.push(this.name);
							html.push("'/>");
							html.push(this.name);
							html.push('</label></div>');
						});
						//初始化全选。
						return so.id('boxRoleForm').html(html.join('')),
						so.checkBoxInit('[selectAllBox]','[selectBox]'),
						$('#selectPermission').modal(),$('#selectRoleId').val(id),!1;
					}else{
						return layer.msg('没有获取到权限数据，请先添加权限数据。',so.default);
					}
				},'json');
			}

			function test(){
				console.log("------>")
            }

			</@shiro.hasPermission>
		</script>
	</head>
	<body data-target="#one" data-spy="scroll">
		<#--引入头部-->
		<@_top.top 4/>
		<div class="container" style="padding-bottom: 15px;min-height: 300px; margin-top: 40px;">
			<div class="row">

				<#--引入左侧菜单-->
				<#--<@_left.role 4/>-->

			<div id="one" class="col-md-2">
                <div class="btn-group-vertical" role="group" aria-label="...">
                    <button type="button" class="btn btn-default">OKEX</button>
                    <button type="button" class="btn btn-default">火币</button>
                    <button type="button" class="btn btn-default">ZB</button>
                </div>
            </div>

				<div class="col-md-10">
					<div clss="well">

					  <button type="button" class="btn btn-default" onclick="$('#trd_symbol').val('btc_usd')">BTC/USDT</button>
					  <button type="button" class="btn btn-default" onclick="$('#trd_symbol').val('eth_usd')">ETH/USDT</button>
					  <button type="button" class="btn btn-default" onclick="$('#trd_symbol').val('eos_usd')">EOS/USDT</button>

					  <table class="table table-bordered">
						  <tr>
							  <th width="7%"><input type="checkbox" name="trd_accounts" id="checkAll_accounts"/>全选</th>
							  <td><input value="tian" check='box_account' type="checkbox" />账户tian</td>
							  <td><input value="acc_2" check='box_account' type="checkbox" />账户A</td>
							  <td><input value="acc_3" check='box_account' type="checkbox" />账户A</td>
							  <td><input value="acc_4" check='box_account' type="checkbox" />账户A</td>
						  </tr>
					  </table>

						  <ul class="list-group">
							  <li class="list-group-item">更新时间：<span  id="trade_curr_tm">...</span></li>
							  <li class="list-group-item">最新成交价：<span  id="trade_ticker">...</span></li>
							  <li class="list-group-item">价格：
								  <input type="text" name="trd_price" id="trd_price" placeholder="价格/price">
							  </li>
							  <li class="list-group-item">数量：
								  <input type="text" name="trd_amount" id="trd_amount" placeholder="数量/amount">
							  </li>
							  <li class="list-group-item">。。。。。。。。。。</li>
						  </ul>


						<input type="text" name="trd_symbol" id="trd_symbol" value="">
						<input type="text" name="trd_type" id="trd_type" value="buy">
					 <span class=""> <#--pull-right -->
						<button type="submit" class="btn btn-primary" onclick="trd_post('buy')">挂买单</button>
						 <button type="submit" class="btn btn-primary" onclick="trd_post('sell')">挂卖单</button>
					 </span>
					</div>

					<hr>
					<table class="table table-bordered">
						<input type="hidden" id="selectRoleId">
						<tr>
							<th width="5%"><input type="checkbox" id="checkAll"/></th>
							<th width="20%">时间</th>
							<th width="10%">平台</th>
							<th width="10%">价格</th>
                            <th width="10%">数量</th>
                            <th width="10%">状态123</th>
							<th width="15%">状态223</th>
                            <th width="30%">操作</th>
						</tr>
						<#if page?exists && page.list?size gt 0 >
							<#list page.list as it>
								<tr>
									<td><input value="${it.id}" check='box' type="checkbox" /></td>
									<td>${it.create_time?string('yyyy-MM-dd hh:mm:ss')}</td>
									<td>${it.coin}</td>
                                    <td>${it.price}</td>
                                    <td>${it.amount}</td>
                                    <td>${it.status}</td>
									<td permissionIds="${it.status?default('')}">${it.status?default('-')}</td>
									<td>
										<@shiro.hasPermission name="/permission/addPermission2Role.shtml">
											<i class="glyphicon glyphicon-share-alt"></i><a href="javascript:selectPermissionById(${it.id});">撤单</a>
										</@shiro.hasPermission>
									</td>
								</tr>
							</#list>
						<#else>
							<tr>
								<td class="text-center danger" colspan="4">没有找到角色</td>
							</tr>
						</#if>
					</table>
					<#if page?exists>
						<div class="pagination pull-right">
							${page.pageHtml}
						</div>
					</#if>

				</div>
			</div><#--/row-->
			
			<#--弹框-->
			<div class="modal fade bs-example-modal-sm"  id="selectPermission" tabindex="-1" role="dialog" aria-labelledby="selectPermissionLabel">
			  <div class="modal-dialog modal-sm" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="selectPermissionLabel">添加权限</h4>
			      </div>
			      <div class="modal-body">
			        <form id="boxRoleForm">
			          loading...
			        </form>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			        <button type="button" onclick="selectPermission();" class="btn btn-primary">Save</button>
			      </div>
			    </div>
			  </div>
			</div>
			<#--/弹框-->
			
		</div>
			
	</body>
</html>