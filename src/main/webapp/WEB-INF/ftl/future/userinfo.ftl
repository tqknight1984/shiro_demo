<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8" />
		<title>账户信息</title>
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
				//初始化全选。1111
				so.checkBoxInit('#checkAll_accounts','[check=account_box]');

				// $('#checkAll_accounts').click();
				<@shiro.hasPermission name="/future/userinfoView.shtml">
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
				<@shiro.hasPermission name="/future/userinfoView.shtml">
				function get_userinfo(){
                    var trd_account = $('#trd_account').val();
					$.post('${basePath}/future/userinfoView.shtml',{trd_account:trd_account},function(result){
						if(result && result.status == 200){
							console.log('===200==='+result.message);

                            var arr = [];
                            arr.push('合约ID:'+result.message.ticker.contract_id);
                            arr.push('买一价:'+result.message.ticker.buy);
                            arr.push('最高价:'+result.message.ticker.high);
                            arr.push('最新成交价:'+result.message.ticker.last);
                            arr.push('最低价:'+result.message.ticker.low);
                            arr.push('卖一价:'+result.message.ticker.sell);
                            arr.push('成交量:'+result.message.ticker.vol);
                            $('#trade_ticker').html(arr.join("  |  "));
							$('#trade_curr_tm').html(result.message.curr_tm);
							setTimeout(init_ticker,1000);
						}else{
							console.log('====500===')
						}
					},'json');
                }
                init_ticker();
				</@shiro.hasPermission>

			});
		</script>
	</head>
	<body data-target="#one" data-spy="scroll">
		<#--引入头部-->
		<@_top.top 6/>
		<div class="container" style="padding-bottom: 15px;min-height: 300px; margin-top: 40px;">
			<div class="row">

				<@_left.userinfo trd_account />

			<#--<div id="one" class="col-md-2">
                <div class="btn-group-vertical" role="group" aria-label="...">
                    <button type="button" class="btn btn-default">OKEX</button>
                    <button type="button" class="btn btn-default">火币</button>
                    <button type="button" class="btn btn-default">ZB</button>
                </div>
            </div>-->

				<div class="col-md-10">
					<div clss="well">

                        <span  id="trade_curr_tm">...</span>
                        <table id="tab_userinfo" class="table table-bordered">
                            <tr>
                                <th >币种</th>
                                <th >账户权益</th>
                                <th >账户余额</th>
                                <th >合约</th>
                            </tr>
                            <#--balance:账户余额
                            available:合约可用
                            balance:账户(合约)余额
                            bond:固定保证金
                            contract_id:合约ID
                            contract_type:合约类别
                            freeze:冻结
                            profit:已实现盈亏
                            unprofit:未实现盈亏
                            rights:账户权益-->
							<#if resultMap.info??>
							<#list resultMap.info?keys as key>
								<tr>
                                    <td >${key}</td>
                                    <td >${resultMap.info[key]["rights"]?string(',###.###')}</td>
                                    <td >${resultMap.info[key]["balance"]?string(',###.###')}</td>
									<#if resultMap.info[key]['contracts']?size gt 0>
										<td >
										<#list resultMap.info[key]['contracts'] as contract>
											类别：${contract["contract_type"]};
                                            冻结：${contract["freeze"]?string(',###.###')};
                                            已实现盈亏：${contract["profit"]?string(',###.###')};
                                            未实现盈亏：${contract["unprofit"]?string(',###.###')};
                                            <br>
										</#list>
										</td>
									<#else>
									<td > ... </td>
									</#if>
                                </tr>
							</#list>
							</#if>
                        </table>
					</div>

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