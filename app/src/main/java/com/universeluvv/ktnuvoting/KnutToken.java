package com.universeluvv.ktnuvoting;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class KnutToken extends AsyncTask<String,String,String> {
    connectToEth connect = new connectToEth();
    private Context view;
    ProgressDialog dialog;
    public KnutToken(Context view){
        this.view = view;
    }

    // Token Transfer Fuction
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
        dialog.setTitle("투표를 하는 중 입니다.");
        dialog.setMessage("모든 투표 과정은 블록체인에 기록되고 있습니다.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        System.out.println(strings[0]);
        System.out.println(strings[1]);

        Web3j web3 = connect.ConnectToTestNet();
        String result = null;
        Credentials credentials = Credentials.create(strings[0]);

        //RawTransactionManager manager     = new RawTransactionManager(web3,credentials);
        TransactionManager manager = new RawTransactionManager(web3,credentials);
        String contractAddr = "0x960B45B4c7DaEA7eeAb67e17c4EdE9446592A143";

        //BigInteger sum = BigInteger.valueOf("1000000000000000000");
        BigInteger sum = new BigInteger("1000000000000000000");
        String data = encodeTransferData(strings[1],sum);

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
            result = txReceipt.getTransactionHash();
            Log.i("txReceipt",txReceipt.getTransactionHash());
        }catch (IOException e ){
            result = "fail";
            return result;
        }catch (TransactionException e){
            result = "fail";
            return result;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        dialog.dismiss();
        super.onPostExecute(s);
    }

    public String Voting (String privateKey, String toAddr){
        Web3j web3 = connect.ConnectToTestNet();
        String result = null;
        Credentials credentials = Credentials.create(privateKey);

        //RawTransactionManager manager     = new RawTransactionManager(web3,credentials);
        TransactionManager manager = new RawTransactionManager(web3,credentials);
        String contractAddr = "0xF155b0A457599918Ce1b20a73ADEc2E9AAE117F0";

        //BigInteger sum = BigInteger.valueOf("1000000000000000000");
        BigInteger sum = new BigInteger("1000000000000000000");
        String data = encodeTransferData(toAddr,sum);

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
            result = txReceipt.getTransactionHash();
            Log.i("txReceipt",txReceipt.getTransactionHash());
        }catch (IOException e ){
            result = "fail";
            return result;
        }catch (TransactionException e){
            result = "fail";
            return result;
        }
        return result;
    }


}
