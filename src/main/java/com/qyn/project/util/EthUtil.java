package com.qyn.project.util;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class EthUtil {
    private static Web3j web3j = Web3j.build(new HttpService(Code.ETH_URL));
    private static Admin admin = Admin.build(new HttpService(Code.ETH_URL));

    private static BigDecimal amout = new BigDecimal("0.01");

    public static List<String> getAccountList() {
        try {
            PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
            List<String> addressList;
            addressList = personalListAccounts.getAccountIds();
            return addressList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendTransaction(String fromAddress) {
        String txHash = null;
        try {
            BigInteger value = Convert.toWei(amout, Convert.Unit.ETHER).toBigInteger();
            if(Code.ETH_TO_ADDRESS == null) throw new Exception("to address null");
            Transaction transaction = Transaction.createEtherTransaction(fromAddress, null, null, null,
                    Code.ETH_TO_ADDRESS, value);
            EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).send();
            txHash = ethSendTransaction.getTransactionHash();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return txHash;
    }
}
