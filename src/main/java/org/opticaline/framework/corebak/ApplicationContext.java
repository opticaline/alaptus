package org.opticaline.framework.corebak;

import org.opticaline.framework.corebak.config.SettingString;
import org.opticaline.framework.corebak.format.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nathan on 14-8-25.
 */
public class ApplicationContext implements Context {
    private Map<Class, Transaction> transactions = new HashMap<Class, Transaction>();
    private Map<Long, HttpServletRequest> httpServletRequests = new HashMap<>();

    public ApplicationContext() {
        this.putDefaultTransactions();
    }

    private void putDefaultTransactions() {
        this.appendTransactions(int.class, new IntTransaction());
        this.appendTransactions(long.class, new LongTransaction());
        this.appendTransactions(boolean.class, new BooleanTransaction());
        this.appendTransactions(char[].class, new CharTransaction());
        this.appendTransactions(float.class, new FloatTransaction());
        this.appendTransactions(double.class, new DoubleTransaction());
        this.appendTransactions(String.class, new StringTransaction());
        this.appendTransactions(String[].class, new StringArrayTransaction());
    }

    public void appendTransactions(Class clazz, Transaction transaction) {
        this.transactions.put(clazz, transaction);
    }

    public Transaction getTransaction(Class clazz) {
        return this.transactions.get(clazz);
    }

    public void setRequest(HttpServletRequest request) {
        this.httpServletRequests.put(Thread.currentThread().getId(), request);
    }

    public HttpServletRequest getRequest() {
        return this.httpServletRequests.get(Thread.currentThread().getId());
    }

    public HttpSession getSession() {
        return this.getRequest().getSession();
    }

    public void setUriParameters(Map<String, Object> parameters) {
        this.getSession().setAttribute(SettingString.URI_PARAMETERS, parameters);
    }

    public Map<String, Object> getUriParameters() {
        Object temp = this.getSession().getAttribute(SettingString.URI_PARAMETERS);
        return temp == null ? new HashMap<String, Object>() : (Map<String, Object>) temp;
    }
}
