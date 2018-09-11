package okcoin.rest;

import com.sojson.common.utils.DateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class MaketTrace {

    private static Logger logger=Logger.getLogger(MaketTrace.class); // 获取logger实例

    private static String _symbol = "eos_usdt";
    private static String _contractType = "quarter";

    final private static long _ticker_tm = 2 * 1000l; //5秒
    final private static int _sycle_size = 30; //相当于3分钟一个周期, 队列中包含2个周期，即 2 * 36 个元素

    final private static int _sycle_trend_limit = 13;  //超过该趋势数即确定趋势
    final private static double _sycle_vol_rate = 2.0d;  //量是上一个周期的2.0倍才确定趋势
    final private static double _sycle_vol_min = 20000d;    //最小量

    //
    private static LinkedList<MarketEle> _queue = new LinkedList<MarketEle>();
    private static int _queue_full_size = _sycle_size * 2;


    //
//    private static StockClient_tq _ok_client = StockClient_tq.getInstance();
    private static FutureClient_tq _ok_client = FutureClient_tq.getInstance();

    private static String _order_id;

    // 步骤标识
    private static String _flag = "init_ele";
    private static long _since = 0l;


    public static void main(String[] args) {
//        int n = 1;
//        JSONArray arr = null;
//        String since = null;
//        while(true){
//
//            try {
//                String str = _ok_client.trades(_symbol, since);
//                logger.info(++n+"->"+str);
//                arr = JSONArray.fromObject(str);
//                if(arr!=null && arr.size()>0){
//                    JSONObject last =  arr.getJSONObject(arr.size()-1);
//                    if(last != null ){
//                        since = last.getString("tid");
//                        long date_ms =  last.getLong("date_ms");
//                        String data_str = DateUtil.dateToString(new Date(date_ms), "yyyy-MM-dd hh:mm:ss:sss");
//                        logger.info("----->"+data_str);
//                    }
//                }
//
//
//
//                Thread.currentThread().sleep(3000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if(false){
//                break;
//            }
//        }

      /*  //add()和remove()方法在失败的时候会抛出异常(不推荐)
        LinkedList<String> queue = new LinkedList<String>();

        //添加元素
        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
        queue.offer("d");
        queue.offer("e");

        logger.info("------>"+((LinkedList<String>) queue).getLast());
        //去除多余元素
        while(queue.size()>2){
            queue.removeFirst();
            logger.info("------>");
            for(String q : queue){
                logger.info(q);
            }
        }*/

//
//        ((LinkedList<String>) queue).removeFirst();
//        logger.info("------>");
//        for(String q : queue){
//            logger.info(q);
//        }


        while (true) {
            try {
                logger.info("begin --------------------" + _flag);
                //初始交易ele
                if ("init_ele".equals(_flag)) {
                    init_ele();
                }
                //判断行情
                if ("get_action".equals(_flag)) {
                    get_action();
                }
                //下单
                if ("trade_up".equals(_flag)) {
                    trade_up();
                }
                if ("trade_down".equals(_flag)) {
                    trade_down();
                }
                //止损止盈
                if ("cancel_up".equals(_flag)) {
                    cancel_up();
                }
                if ("cancel_down".equals(_flag)) {
                    cancel_down();
                }

                //订单确认
                if ("check_ord".equals(_flag)) {
                    check_ord();
                }
                //
                Thread.currentThread().sleep(_ticker_tm);
                logger.info("-------------------- end");
            } catch (InterruptedException e) {
                _flag = "init_ele";
                e.printStackTrace();
            }
        }

    }

    private static void check_ord() {
    }

    private static void cancel_down() {
    }

    private static void cancel_up() {
    }

    private static void trade_down() {
    }

    private static void trade_up() {
    }

    private static void get_action() {
        if(_order_id != null){
            _flag="init_ele";
            return;
        }

        MarketEle lastEle = _queue.getLast();
        //test
        String out_str = "判断趋势-------down: %s >? %s; up: %s >? %s; vol:%s/%s=%s >? %s";
        logger.info(String.format(out_str, lastEle.sycle_down_count , _sycle_trend_limit,lastEle.sycle_up_count , _sycle_trend_limit, lastEle.sycle_vol, lastEle.last_sycle_ele.sycle_vol, String.format("%.2f", lastEle.sycle_vol/lastEle.last_sycle_ele.sycle_vol), _sycle_vol_rate));

        if(lastEle.sycle_vol/lastEle.last_sycle_ele.sycle_vol < _sycle_vol_rate){
            _flag = "init_ele";
            return;
        }

        String action = null;
        if(lastEle.sycle_down_count > _sycle_trend_limit){
            action = "down";
        }
        if(lastEle.sycle_up_count > _sycle_trend_limit){
            action = "up";
        }

        if(action == null){
            _flag = "init_ele";
            return;
        }


        logger.info(DateUtil.getCurrentTimeString()+"------趋势确定----"+action);

//        //  vol也满足
//        if(lastEle.sycle_vol < _sycle_vol_min || lastEle.sycle_vol < _sycle_vol_min){
//            logger.info("交易量不达标");
//            _flag = "init_ele";
//            return;
//        }




        _flag = "init_ele";
    }

//                arr = JSONArray.fromObject(str);
//                if(arr!=null && arr.size()>0){
//                    JSONObject last =  arr.getJSONObject(arr.size()-1);
//                    if(last != null ){
//                        since = last.getString("tid");
//                        long date_ms =  last.getLong("date_ms");
//                        String data_str = DateUtil.dateToString(new Date(date_ms), "yyyy-MM-dd hh:mm:ss:sss");
//                        logger.info("----->"+data_str);
//                    }
//                }


    private static void init_ele() {
        MarketEle ele = new MarketEle();
        try {
//            String trades_str = _ok_client.trades(_symbol, _since);
            //
            String trades_str = _ok_client.trades(_symbol, _contractType);
//            logger.info(DateUtil.getCurrentTimeString()+"--trade-->" + trades_str);
            ele.trades_str = trades_str;
            JSONArray arr = JSONArray.fromObject(trades_str);
            //期货需要手动去重
            if(_since != 0l){
                JSONArray new_arr = new JSONArray();
                for (Object obj : arr) {
                    if( ((JSONObject) obj).getLong("tid") > _since){
                        new_arr.add(obj);
                    }
               }
                arr = new_arr;
            }

            //
            int trade_len = 0;
            double ele_last = 0.0d;
            double ele_vol = 0.0d;
            if (arr != null && arr.size() > 0) {
                trade_len = arr.size();
                JSONObject last = arr.getJSONObject(arr.size() - 1);
                _since = last.getLong("tid");
                ele_last = last.getDouble("price");
                //
                for (Object obj : arr) {
                    double amount = ((JSONObject) obj).getDouble("amount");
                    ele_vol = ele_vol + amount;
                }
            }
            ele.init(trade_len, ele_last, ele_vol);
        } catch (Exception e) {
            e.printStackTrace();
            //
            _queue.offer(ele);
            _flag = "init_ele";
            //去除多余元素
            while (_queue.size() > _queue_full_size) {
                _queue.removeFirst();
            }
            return;
        }

        //额外信息
        MarketEle lastEle = null;
        try {
            lastEle = _queue.getLast();
        } catch (NoSuchElementException e) {
            //nothing to do
        }

        if (lastEle == null) {
            _flag = "init_ele";
            _queue.offer(ele);
            return;
        }

        //如果当前ele没有数据，则最新价格延续上一个ele
        if (ele.trade_len <= 0 || ele.last <= 0.0d || ele.curr_vol <= 0.0d) {
            ele.last = lastEle.last;
            ele.curr_trend = "keep";
        }

        //
        int sycle_up_count = 0;
        int sycle_down_count = 0;
        int sycle_keep_count = 0;

        if (lastEle.last == ele.last) {
            ele.curr_trend = "keep";
            sycle_keep_count = 1;       //趋势统计包含当前ele
        } else if (lastEle.last < ele.last) {
            ele.curr_trend = "up";
            sycle_up_count = 1;
        } else if (lastEle.last > ele.last) {
            ele.curr_trend = "down";
            sycle_down_count = 1;
        } else {
            _flag = "err_001";
            _queue.offer(ele);
            return;
        }

        //上个周期ele
        int queue_size = _queue.size();
        int sycle_begin_idx = 0;
        if (queue_size == _sycle_size) {
            sycle_begin_idx = 1;
            ele.last_sycle_ele = _queue.get(0);
        } else if (queue_size > _sycle_size){
            sycle_begin_idx = queue_size - _sycle_size + 1;
            ele.last_sycle_ele = _queue.get(queue_size - _sycle_size);
        } else {
            sycle_begin_idx = 0;
            ele.last_sycle_ele = null;
        }

        //周期内趋势统计, 周期交易量
        double sycle_vol = ele.curr_vol;
        for (int i = sycle_begin_idx; i < queue_size; i++) {
            MarketEle tmp_ele = _queue.get(i);
            sycle_vol = sycle_vol + tmp_ele.curr_vol;
            if ("keep".equals(tmp_ele.curr_trend)) {
                sycle_keep_count++;
            } else if ("up".equals(tmp_ele.curr_trend)) {
                sycle_up_count++;
            } else if ("down".equals(tmp_ele.curr_trend)) {
                sycle_down_count++;
            } else {
                _flag = "err_002";
                _queue.offer(ele);
                return;
            }
        }
        ele.sycle_keep_count = sycle_keep_count;
        ele.sycle_up_count = sycle_up_count;
        ele.sycle_down_count = sycle_down_count;
        ele.sycle_vol = sycle_vol;

        //
        int size = _queue.size();
        if (size == _queue_full_size) {
            _flag = "get_action";
        } else if (size > _queue_full_size) {
            _queue.removeFirst();   //去除多余元素
            _flag = "get_action";
        } else if (size < _queue_full_size) {
            _flag = "init_ele";
        } else {
            _flag = "err_01";
            return;
        }
        //
        _queue.offer(ele);
        logger.info(DateUtil.getCurrentTimeString()+"--ele-->" + ele.toString());
    }

}


//
class MarketEle{

    public int trade_len;
    public String trades_str;
    public String init_tm;

    public double last;

    //额外信息
    public String curr_trend="keep"; //与上一个比，趋势 up/down/keep
    public double curr_vol; //当前量

    //一个周期跌次
    public int sycle_up_count;//一个周期涨次
    public int sycle_down_count;//一个周期跌次
    public int sycle_keep_count;//一个周期平次

    public double sycle_vol; //一个周期成交量
    public MarketEle last_sycle_ele; //上个周期ele


    public void init(int trade_len, double ele_last, double ele_vol) {
        init_tm = DateUtil.getCurrentTimeString();
        this.trade_len = trade_len;
        this.last = ele_last;
        this.curr_vol = ele_vol;
    }

    @Override
    public String toString() {
        return "MarketEle{" +
                "init_tm=" + init_tm +
                ", trade_len=" + trade_len +
                ", last=" + last +
                ", curr_trend='" + curr_trend + '\'' +
                ", curr_vol=" + curr_vol +
                ", sycle_up_count=" + sycle_up_count +
                ", sycle_down_count=" + sycle_down_count +
                ", sycle_keep_count=" + sycle_keep_count +
                ", sycle_vol=" + sycle_vol +
                ", last_sycle_ele=" + last_sycle_ele +
                '}';
    }
}
