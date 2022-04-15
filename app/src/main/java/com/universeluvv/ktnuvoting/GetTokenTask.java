package com.universeluvv.ktnuvoting;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.universeluvv.ktnuvoting.InfoClass.VoteDetailInfo;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public class GetTokenTask extends AsyncTask<String,String,String> {

    connectToEth connect = new connectToEth();
    private Context view;
    ProgressDialog dialog;

    VoteDetailInfo voteDetailInfo;
    boolean getcoin;
    String major, id,votename;

    int i;


    public GetTokenTask(Context view, String major, String id,VoteDetailInfo voteDetailInfo,boolean getcoin,int i) {
        this.view = view;
        this.major = major;
        this.id = id;
        this.voteDetailInfo =voteDetailInfo;
        this.getcoin =getcoin ;
        this.i =i;
    }

    public GetTokenTask(Context context,boolean getcoin, String major, String id, String votename) {
        this.view =context;
        this.getcoin = getcoin;
        this.major = major;
        this.id = id;
        this.votename = votename;
    }


    public String encodeTransferData(String Address, BigInteger sum){
        Function function = new Function(
                "transfer", // 호출 할 메소드
                Arrays.asList(new Address(Address),new Uint256(sum)),
                Arrays.asList(new org.web3j.abi.TypeReference<Bool>() {}));
        return FunctionEncoder.encode(function);
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(view);
        dialog.setTitle("트랜잭션을 생성하고 있습니다.");
        dialog.setMessage("잠시만 기다려 주세요.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... strings) {
        Web3j web3 = connect.ConnectToTestNet();
        String result = null;
        String privateKey = "8BEBA8BBBC0E8D1C3E06C3939F9E83B7D8F2E806FB23387D4BF76C68901BF48F";
        Credentials credentials = Credentials.create(privateKey);

        TransactionManager manager = new RawTransactionManager(web3,credentials);
        String contractAddr = "0x960B45B4c7DaEA7eeAb67e17c4EdE9446592A143";

        BigInteger sum = new BigInteger("1000000000000000000");
        String data = encodeTransferData(strings[0],sum);
        try{
            String txHash = manager.sendTransaction(
                    DefaultGasProvider.GAS_PRICE,
                    DefaultGasProvider.GAS_LIMIT,
                    contractAddr,
                    data,
                    BigInteger.ZERO).getTransactionHash();

            Log.i("txHash",txHash);
            TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(
                    web3,
                    TransactionManager.DEFAULT_POLLING_FREQUENCY,
                    TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH);
            TransactionReceipt txReceipt = receiptProcessor.waitForTransactionReceipt(txHash);
            Log.i("txReceipt",txReceipt.getTransactionHash());

        }catch (IOException e ){
            result = "fail";
        }catch (TransactionException e){
            result = "fail";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        dialog.dismiss();
        DatabaseReference ds =FirebaseDatabase.getInstance().getReference();
        if(getcoin) {
            Toast.makeText(view,"투표권이 생성되었습니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            ds.child("vote").child(voteDetailInfo.getName()).child("item").child(voteDetailInfo.getItem().get(i)).setValue(voteDetailInfo.getItem_cnt().get(i)+1);
            ds.child("vote").child(voteDetailInfo.getName()).child("current_cnt").setValue(voteDetailInfo.getCurrent_cnt()+1);
            ds.child("student").child(major).child(id).child("votechecking").child(voteDetailInfo.getName()).setValue(1);
            ds.child("student").child(major).child(id).child("coin").child(voteDetailInfo.getName()).removeValue();
            Toast.makeText(view,"투표가 완료되었습니다.",Toast.LENGTH_SHORT).show();
        }

        super.onPostExecute(s);
    }
}
