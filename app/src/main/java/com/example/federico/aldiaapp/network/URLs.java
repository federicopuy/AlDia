package com.example.federico.aldiaapp.network;

public class URLs {

    private static final String URLBASE = "http://66.97.34.149:";
    private static final String PORT = "8080";
    private static final String APIURL = "/aldia/api/";
    public static final String APIURLCOMPLETE = URLBASE + PORT + APIURL;
    public static final String AUTHENTICATE = "authenticateApp";
    public static final String BUSINESSES = "comercios";
    public static final String PAYMENTS = "liquidacions";
    public static final String EMPLOYEES = "empleados";
    public static final String GET_EMPLOYEE = "getEmpleado";
    public static final String ONE = "one";
    public static final String ALL = "all";
    public static final String SHIFTS = "periodos";
    public static final String NEW = "new";
    public static final String SEARCH_METHOD_BY_PAYMENT_ID = "findByLiquidacion";
    public static final String SEARCH_METHOD_PENDING_TO_BE_PAID = "withoutLiquidacion";
  public static final String REGISTER_PERIOD_OFFLINE = "registrarPeriodo";
}
