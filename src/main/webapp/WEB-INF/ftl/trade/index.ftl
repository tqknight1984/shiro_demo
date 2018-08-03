<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8" />
		<title>币币交易</title>
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
				so.checkBoxInit('#checkAll_accounts','[check=account_box]');
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
				function init_ticker(){
					$.post('${basePath}/trade/ticker.shtml',{symbol:'btc_usd'},function(result){
						if(result && result.status == 200){
							console.log('===200==='+result.message);
							$('#trade_ticker').html(result.message.ticker.last);
							$('#trade_curr_tm').html(result.message.curr_tm);
							setTimeout(init_ticker,1000);
						}else{
							console.log('====500===')
						}
					},'json');
                }
                init_ticker();
				</@shiro.hasPermission>

				//默认全部选中
				var check_ls = $('[check=account_box]');
                check_ls.each(function(){
                    this.checked="checked";
                });

                //默认btc_usd
				$('#btc_usd_btn').click()

			});

            //下单
			<@shiro.hasPermission name="/trade/ticker.shtml">
			function trd_post(trd_type){
                var trd_symbol = $('#trd_symbol').val();
				if($.trim(trd_symbol) == '' || trd_symbol.length<3){
                    return layer.msg('交易对有误。');
                }
                trd_price  = $('#trd_price').val();
                trd_amount  = $('#trd_amount').val();
                if($.trim(trd_price) == ''){
                    return layer.msg('价格不能为空。');
                }
                if($.trim(trd_amount) == ''){
                    return layer.msg('数量不能为空。');
                }

                var trd_accounts = "";
                var checkeds = $('[check=account_box]:checked');
                console.log(checkeds.length);
                $.each(checkeds,function(){
                    //console.log(this.value);
                    trd_accounts = trd_accounts+(this.value+",")
                });
                if($.trim(trd_accounts) == '' || trd_accounts.length < 1 ){
                    return layer.msg('请选中操作账号');
                }

                var index = layer.confirm(trd_symbol+ "确定下单？"+ trd_type +" 价格："+ trd_price +" 数量："+trd_amount,function(){
                	var load = layer.load();
					$.post('${basePath}/trade/trade.shtml',
							{trd_symbol:trd_symbol, trd_type:trd_type,trd_price:trd_price,trd_amount:trd_amount,trd_accounts:trd_accounts},
							function(result){
						layer.close(load);
						if(result && result.status != 200){
							return layer.msg(result.message,so.default),!1;
						}
						//添加行
						var res_ls = result.message;
						$.each(res_ls, function(k, trd){
						    console.log("---下单res----333--->"+trd.order_id);
						    if(trd.order_id && trd.order_id.length > 2){
								var tr_html=[];
								tr_html.push('<tr id=\'order_'+trd.order_id+'\'>');
								tr_html.push('<td>'+trd.create_tm+'</td>');
								tr_html.push('<td>'+trd.site+'</td>');
								tr_html.push('<td>'+trd.account+'</td>');
								tr_html.push('<td>'+trd.type.toUpperCase()+'</td>');
								tr_html.push('<td>'+trd.price+'</td>');
								tr_html.push('<td>'+trd.amount+'</td>');
								tr_html.push('<td>'+trd.status+'</td>');
								if(trd.status=="OK"){
									tr_html.push('<td><i class="glyphicon glyphicon-share-alt"></i><a href="javascript:cancelOrder('+ trd.id + ', \''+ trd.account+ '\','+ trd.order_id +');">撤单</a></td></tr>');
								}else{
									tr_html.push('<td></td></tr>');
								}
								// $('#tab_trade').append(tr_html.join(""));
								$("#tab_trade tr:eq(0)").after(tr_html.join(""));
								$("#order_"+trd.order_id).hide();
								$("#order_"+trd.order_id).fadeIn("slow",function () {
									$(this).show();
								});
                            }
						});

						if(result.errStr){
                            layer.msg(result.errStr);
						}

					},'json');
                    layer.close(index);
                });
            }

            //撤单
			function cancelOrder(trd_id, trd_account, order_id){
                var trd_symbol = $('#trd_symbol').val();
                if($.trim(trd_symbol) == '' || trd_symbol.length<3){
                    return layer.msg('交易对有误。');
                }
				var load = layer.load();
				$.post("${basePath}/trade/cancel_order.shtml",{trd_id: trd_id, trd_account:trd_account, symbol:trd_symbol,order_id:order_id},function(result){
					layer.close(load);
					if(result && result.status == 200){
					    var order_id = result.order_id;
                        $("#order_" + order_id).fadeOut("slow", function (){
                            $(this).remove();
                        });
					}else{
						return layer.msg('撤单失败。'+result.message,so.default);
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

					  <button id="btc_usd_btn" type="button" class="btn btn-default" onclick="$('#trd_symbol').val('btc_usdt')">BTC/USDT</button>
					  <button type="button" class="btn btn-default" onclick="$('#trd_symbol').val('eth_usdt')">ETH/USDT</button>
					  <button type="button" class="btn btn-default" onclick="$('#trd_symbol').val('eos_usdt')">EOS/USDT</button>

					  <table class="table table-bordered">
						  <tr>
							  <th width="7%"><input type="checkbox"  id="checkAll_accounts"/>全选</th>
							  <td><input value="tian" check='account_box' type="checkbox" />账户-tian</td>
							  <td><input value="guoguo" check='account_box' type="checkbox" />账户-guoguo</td>
							  <td><input value="acc_3" check='account_box' type="checkbox" />账户A</td>
							  <td><input value="acc_4" check='account_box' type="checkbox" />账户B</td>
						  </tr>
					  </table>

                        <table class="table table-bordered">
                            <tr>
								<td width="50%">更新时间：<span  id="trade_curr_tm">...</span></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>最新成交价：<span  id="trade_ticker">...</span></td>
                                <td > 价格：<input type="text" name="trd_price" id="trd_price" placeholder="价格/price"></td>
                            </tr>
                            <tr>
                                <td>当前平台：<input type="text" name="x" id="x" value="" placeholder="OKEX" readonly></td>

                                <td>数量：<input type="text" name="trd_amount" id="trd_amount" placeholder="数量/amount"></td>
                            </tr>
                            <tr>
                                <td>当前交易对：<input type="text" name="trd_symbol" id="trd_symbol" value="" placeholder="btc_usdt" readonly></td>
                                <td >
									<button type="submit" class="btn btn-primary" onclick="trd_post('buy')">挂买单</button>
                                	<button type="submit" class="btn btn-success" onclick="trd_post('sell')">挂卖单</button>
								</td>
                            </tr>
                        </table>
					</div>

					<table id="tab_trade" class="table table-bordered">
						<tr>
							<th width="20%">时间</th>
							<th width="10%">平台</th>
							<th width="10%">账号</th>
                            <th width="10%">买卖</th>
							<th width="10%">价格</th>
                            <th width="10%">数量</th>
							<th width="10%">交易对</th>
							<th width="10%">状态</th>
                            <th width="10%">操作</th>
						</tr>
						<#if resultMap?exists && resultMap.trade_ls?size gt 0 >
							<#list resultMap.trade_ls as trd>
								<tr id="order_${trd.order_id}">
									<td>${trd.create_tm}</td>
									<td>${trd.site}</td>
                                    <td>${trd.account}</td>
                                    <td>${trd.type}</td>
                                    <td>${trd.price}</td>
                                    <td>${trd.amount}</td>
                                    <td>${trd.symbol}</td>
                                    <td>${trd.status}</td>
									<td>
									<#if trd.status == "OK" >
										<i class="glyphicon glyphicon-share-alt"></i><a href="javascript:cancelOrder(${trd.id}, '${trd.account}',${trd.order_id});">撤单</a>
									</#if>
									</td>
								</tr>
							</#list>
						<#else>
							<tr>
								<td class="text-center danger" colspan="9">没有挂单记录</td>
							</tr>
						</#if>
					</table>
					<#if page?exists>
						<div class="pagination pull-right">
                            pageHtml

							<#--${page.pageHtml}-->
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