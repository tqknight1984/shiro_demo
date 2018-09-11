<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8" />
		<title>合约交易</title>
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
				<@shiro.hasPermission name="/future/index.shtml">
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
				<@shiro.hasPermission name="/future/index.shtml">
				function init_ticker(){
                    var trd_symbol = $('#trd_symbol').val();
                    var trd_contract_type = $('#trd_contract_type').val();
					$.post('${basePath}/future/ticker.shtml',{symbol:trd_symbol, contractType:trd_contract_type},function(result){
						if(result && result.status == 200){
							console.log('===200==='+result.message);
                            // buy:买一价
                            // contract_id:合约ID
                            // high:最高价
                            // last:最新成交价
                            // low:最低价
                            // sell:卖一价
                            // unit_amount:合约面值
                            // vol:成交量(最近的24小时)
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

				//默认全部选中
				var check_ls = $('[check=account_box]');
                check_ls.each(function(){
                    this.checked="checked";
                });

			});

            //下单
			<@shiro.hasPermission name="/future/index.shtml">
			function trd_post(trd_type){
                var trd_symbol = $('#trd_symbol').val();
				if($.trim(trd_symbol) == '' || trd_symbol.length<3){
                    return layer.msg('交易对有误。');
                }
                var trd_contract_type = $('#trd_contract_type').val();
                if($.trim(trd_contract_type) == '' || trd_contract_type.length<3){
                    return layer.msg('合约类型有误。');
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
                $.each(checkeds,function(){
                    console.log("-->"+this.value);
                    trd_accounts = trd_accounts+(this.value+",")
                });
                console.log("trd_accounts--->"+trd_accounts);
                if($.trim(trd_accounts) == '' || trd_accounts.length < 1 ){
                    return layer.msg('请选中操作账号');
                }

                var index = layer.confirm(trd_symbol+ "确定下单？"+ trd_type +" 价格："+ trd_price +" 数量："+trd_amount,function(){
                	var load = layer.load();
					$.post('${basePath}/future/trade.shtml',
							{trd_symbol:trd_symbol,trd_contract_type:trd_contract_type,trd_price:trd_price,trd_amount:trd_amount,trd_type:trd_type,trd_accounts:trd_accounts},
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
						        var contractType = "";
                                if(trd.contract_type == "this_week"){
                                    contractType ="当周";
                                }else if(trd.contract_type == "next_week"){
                                    contractType ="次周";
                                }else if(trd.contract_type == "month"){
                                    contractType ="当月";
                                }else if(trd.contract_type == "quarter"){
                                    contractType ="季度";
                                }else{
                                    contractType = "";
                                }
						        var type = "";
                                if(trd.type == "1"){
                                    type ="开多";
                                }else if(trd.type == "2"){
                                    type ="开空";
                                }else if(trd.type == "3"){
                                    type ="平多";
                                }else if(trd.type == "4"){
                                    type ="平空";
                                }else{
                                    type = "";
                                }

								var tr_html=[];
								tr_html.push('<tr id=\'order_'+trd.order_id+'\'>');
								tr_html.push('<td>'+trd.create_tm+'</td>');
								tr_html.push('<td>'+trd.site+'</td>');
								tr_html.push('<td>'+trd.account+'</td>');
                                tr_html.push('<td>'+contractType+'</td>');
								tr_html.push('<td>'+type+'</td>');
								tr_html.push('<td>'+trd.price+'</td>');
								tr_html.push('<td>'+trd.amount+'</td>');
								tr_html.push('<td>'+trd.symbol+'</td>');
								tr_html.push('<td>'+trd.status+'</td>');
								if(trd.status=="OK"){
									tr_html.push('<td><i class="glyphicon glyphicon-share-alt"></i><a href="javascript:cancelOrder('+ trd.id + ', \''+ trd.account+ '\',\''+ trd.contract_type+ '\','+ trd.order_id +');">撤单</a></td></tr>');
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
			function cancelOrder(trd_id, trd_account, contract_type, order_id){
                var trd_symbol = $('#trd_symbol').val();
                if($.trim(trd_symbol) == '' || trd_symbol.length<3){
                    return layer.msg('交易对有误。');
                }
                if($.trim(contract_type) == '' || contract_type.length<3){
                    return layer.msg('交易对有误。');
                }
				var load = layer.load();
				$.post("${basePath}/future/cancel_order.shtml",{trd_id: trd_id, trd_account:trd_account, symbol:trd_symbol,contractType:contract_type,order_id:order_id},function(result){
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

			</@shiro.hasPermission>
		</script>
	</head>
	<body data-target="#one" data-spy="scroll">
		<#--引入头部-->
		<@_top.top 5/>
		<div class="container" style="padding-bottom: 15px;min-height: 300px; margin-top: 40px;">
			<div class="row">

				<#--引入左侧菜单-->
				<@_left.future 1/>

			<#--<div id="one" class="col-md-2">
                <div class="btn-group-vertical" role="group" aria-label="...">
                    <button type="button" class="btn btn-default">OKEX</button>
                    <button type="button" class="btn btn-default">火币</button>
                    <button type="button" class="btn btn-default">ZB</button>
                </div>
            </div>-->

				<div class="col-md-10">
					<div clss="well">
						选择交易对：
					  <button id="btc_usd_btn" type="button" class="btn btn-default" onclick="$('#trd_symbol').val('btc_usdt')">BTC/USDT</button>
					  <button type="button" class="btn btn-default" onclick="$('#trd_symbol').val('eth_usdt')">ETH/USDT</button>
					  <button type="button" class="btn btn-default" onclick="$('#trd_symbol').val('eos_usdt')">EOS/USDT</button>

					选择合约类型对：<#--this_week:当周   next_week:下周   month:当月   quarter:季度-->
					<button id="btc_usd_btn" type="button" class="btn btn-default" onclick="$('#trd_contract_type_view').val('当周');$('#trd_contract_type').val('this_week')">当周</button>
					<button type="button" class="btn btn-default" onclick="$('#trd_contract_type_view').val('下周');$('#trd_contract_type').val('next_week')">下周</button>
					<button type="button" class="btn btn-default" onclick="$('#trd_contract_type_view').val('当月');$('#trd_contract_type').val('month')">当月</button>
					<button type="button" class="btn btn-default" onclick="$('#trd_contract_type_view').val('季度');$('#trd_contract_type').val('quarter')">季度</button>

					  <table class="table table-bordered">
						  <tr>
							  <th width="7%"><input type="checkbox"  id="checkAll_accounts"/>全选</th>
							  <td><input value="tian" check='account_box' type="checkbox" />账户-tian</td>
							  <td><input value="guoguo" check='account_box' type="checkbox" />账户-guoguo</td>
							  <td><input value="xianyang" check='account_box' type="checkbox" />账户-xianyang</td>
							  <td><input value="acc_4" check='account_box' type="checkbox" />账户B</td>
						  </tr>
					  </table>

                        <span  id="trade_curr_tm">...</span>
                        <table class="table table-bordered">
                            <tr>
								<td colspan="2">
                                    最新成交价：<span  id="trade_ticker">...</span>
								</td>
                            </tr>
                            <tr>
                                <td  width="50%">当前平台：<input type="text" name="x" id="x" value="OKEX" readonly></td>
                                <td > 价格：<input type="text" name="trd_price" id="trd_price" placeholder="价格/price"></td>
                            </tr>
                            <tr>
                                <td>当前合约类型：
									<input type="text" id="trd_contract_type_view" value="季度" readonly>
                                    <input type="hidden" id="trd_contract_type" value="quarter" readonly>
								</td>
                                <td>数量：<input type="text" name="trd_amount" id="trd_amount" placeholder="数量/amount"></td>
                            </tr>
                            <tr>
                                <td>当前交易对：<input type="text" name="trd_symbol" id="trd_symbol" value="btc_usdt" readonly></td>
                                <td >
									<#--1:开多   2:开空   3:平多   4:平空-->
									<button type="submit" class="btn btn-primary" onclick="trd_post('1')">开多</button>
                                	<button type="submit" class="btn btn-success" onclick="trd_post('2')">开空</button>
                                	<button type="submit" class="btn btn-primary" onclick="trd_post('3')">平多</button>
                                	<button type="submit" class="btn btn-success" onclick="trd_post('4')">平空</button>
								</td>
                            </tr>
                        </table>
						<span>杠杆倍数，下单时无需传送，系统取用户在页面上设置的杠杆倍数。且“开仓”若有10倍多单，就不能再下20倍多单</span>
					</div>

					<table id="tab_trade" class="table table-bordered">
						<tr>
							<th >时间</th>
							<th >平台</th>
							<th >账号</th>
                            <th >类型</th>
                            <th >买卖</th>
							<th >价格</th>
                            <th >数量</th>
							<th >交易对</th>
							<th >状态</th>
                            <th >操作</th>
						</tr>
						<#if resultMap?exists && resultMap.trade_ls?size gt 0 >
							<#list resultMap.trade_ls as trd>
								<tr id="order_${trd.order_id}">
									<td>${trd.create_tm}</td>
									<td>${trd.site}</td>
                                    <td>${trd.account}</td>
                                    <td>
										<#if trd.contract_type == "this_week">当周
										<#elseif trd.contract_type == "next_week">次周
										<#elseif trd.contract_type == "month">当月
										<#elseif trd.contract_type == "quarter">季度
										</#if>
									</td>
                                    <td>
										<#if trd.type == "1">开多
										<#elseif trd.type == "2">开空
										<#elseif trd.type == "3">平多
										<#elseif trd.type == "4">平空
										</#if>
									</td>
                                    <td>${trd.price}</td>
                                    <td>${trd.amount}</td>
                                    <td>${trd.symbol}</td>
                                    <td>${trd.status}</td>
									<td>
									<#if trd.status == "OK" >
										<i class="glyphicon glyphicon-share-alt"></i><a href="javascript:cancelOrder(${trd.id}, '${trd.account}','${trd.contract_type}',${trd.order_id});">撤单</a>
									</#if>
									</td>
								</tr>
							</#list>
						<#else>
							<tr>
								<td class="text-center danger" colspan="10">没有挂单记录</td>
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