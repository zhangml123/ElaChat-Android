package com.eladapp.elachat.wallet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.breadwallet.presenter.entities.TxUiHolder;
import com.breadwallet.tools.manager.BRSharedPrefs;
import com.breadwallet.wallet.WalletsMaster;
import com.breadwallet.wallet.abstracts.BaseWalletManager;
import com.eladapp.elachat.R;
import com.eladapp.elachat.manager.WalletElaManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TxlistAdapter extends BaseAdapter {
    private Context context;
    private List<TxUiHolder> datas;
    public TxlistAdapter(Context context , List<TxUiHolder> datas){
        this.context = context;
        this.datas = datas;
    }
    public final class ViewHolder
    {
        public LinearLayout txinfo;
        public ImageView txidimg;
        public TextView txidconfirmstatus;
        public TextView txamount;
        public TextView txtime;
    }
    @SuppressLint("NewApi")
    public View getView(int i, View view, ViewGroup viewGroup) {
        final TxlistAdapter.ViewHolder vh;
        if (view == null) {
            vh = new TxlistAdapter.ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_txlist_item, null);
            vh.txinfo = (LinearLayout) view.findViewById(R.id.txinfo);
            vh.txidimg = (ImageView) view.findViewById(R.id.txidimg);
            vh.txidconfirmstatus = (TextView)view.findViewById(R.id.txidconfirmstatus);
            vh.txamount = (TextView)view.findViewById(R.id.txamount);
            vh.txtime = (TextView)view.findViewById(R.id.txtime);
            view.setTag(vh);

            if(!datas.get(i).isReceived()){
                if(!getStatus(datas.get(i))){
                    vh.txidimg.setImageResource(R.drawable.up_gray);
                    if(getlangconfig().equals("cn")) {
                        vh.txidconfirmstatus.setText("确认中");
                    }else if(getlangconfig().equals("en")){
                        vh.txidconfirmstatus.setText("Confirming");
                    }else{
                        vh.txidconfirmstatus.setText("确认中");
                    }
                }else{
                    vh.txidimg.setImageResource(R.drawable.up_green);
                    if(getlangconfig().equals("cn")) {
                        vh.txidconfirmstatus.setText("已确认");
                    }else if(getlangconfig().equals("en")){
                        vh.txidconfirmstatus.setText("Confirmed");
                    }else{
                        vh.txidconfirmstatus.setText("已确认");
                    }
                }
                vh.txamount.setText("-"+String.valueOf(datas.get(i).getAmount())+"ELA");
            }else {
                if(!getStatus(datas.get(i))){
                    vh.txidimg.setImageResource(R.drawable.down_gray);
                    if(getlangconfig().equals("cn")) {
                        vh.txidconfirmstatus.setText("确认中");
                    }else if(getlangconfig().equals("en")){
                        vh.txidconfirmstatus.setText("Confirming");
                    }else{
                        vh.txidconfirmstatus.setText("确认中");
                    }
                }else {
                    vh.txidimg.setImageResource(R.drawable.down_green);
                    if(getlangconfig().equals("cn")) {
                        vh.txidconfirmstatus.setText("已确认");
                    }else if(getlangconfig().equals("en")){
                        vh.txidconfirmstatus.setText("Confirmed");
                    }else{
                        vh.txidconfirmstatus.setText("已确认");
                    }
                }
                vh.txamount.setText("+"+String.valueOf(datas.get(i).getAmount())+"ELA");
            }
            SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
            //System.out.println(time.format(System.currentTimeMillis()));
            vh.txtime.setText(String.valueOf(time.format(new Date(Long.valueOf(String.valueOf(datas.get(i).getTimeStamp()+"000"))))));
        }
        return view;
    }
    public String getlangconfig(){
        String lang = "";
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        String langs = String.valueOf(config.locale);
        if(langs.equals("cn")){
            lang = "cn";
        }else if(langs.equals("en")){
            lang = "en";
        }else{
            lang = "cn";
        }
        return lang;
    }
    public TxUiHolder getItem(int position) {
        return datas.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public int getCount() {
        return datas.size();
    }
    public boolean getStatus(TxUiHolder item){
        WalletElaManager wm = WalletElaManager.getInstance(context);
        int blockHeight = item.getBlockHeight();
        int lastBlockHeight = BRSharedPrefs.getLastBlockHeight(context, wm.getIso());
        System.out.println("Integer.MAX_VALUE："+Integer.MAX_VALUE);
        int confirms = blockHeight == Integer.MAX_VALUE ? 0 : lastBlockHeight - blockHeight + 1;
        System.out.println("confirms11111："+confirms);
        int level;
        if (confirms <= 0) {
            long relayCount = wm.getRelayCount(item.getTxHash());
            if (relayCount <= 0)
                level = 0;
            else if (relayCount == 1)
                level = 1;
            else
                level = 2;
        } else {
            if (confirms >= 4) {
                level = 6;
            } else {
                level = confirms + 2;
            }
        }
        System.out.println("level1111："+level);
        if(level == 6 ){
            return true;
        }else{
            return false;
        }
    }
}