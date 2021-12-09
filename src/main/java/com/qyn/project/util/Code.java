package com.qyn.project.util;

public class Code {
    public static String ERROR = "ERROR";

    public final static int REDIS_COMPANY = 2001;
    public final static int REDIS_PRODUCTION = 2002;
    public static String REDIS_COMPANY_N2C_MAP = "company_n2c_map";
    public static String REDIS_COMPANY_C2N_MAP = "company_c2n_map";
    public static String REDIS_PRODUCTION_N2C_MAP = "production_n2c_map";
    public static String REDIS_PRODUCTION_C2N_MAP = "production_c2n_map";

    public static String REDIS_SERIAL_INIT = "5000";

    public static String REDIS_PRODUCTION_LIST = "production_list";

    public final static int companyCodeBitLen = 27;
    public final static int nameCodeBitLen = 17;
    public final static int UIDSerialBitLen = 60;

    public final static int companyCodeLen = 8;
    public final static int nameCodeLen = 4;

    public final static String MAPPING_OK = "3001";
    public static String MAPPING_KEY_EXIST = "3002";
    public static String MAPPING_VALUE_EXIST = "3003";
    public static String MAPPING_LEN_ERROR = "3004";

    public static String OK = "成功";
    public static String MAPPING_KEY_EXIST_MES = "key已存在";
    public static String MAPPING_VALUE_EXIST_MES = "value已存在";
    public static String MAPPING_LEN_ERROR_MES = "代码长度不符";

    public static String RELATION_TYPE_COMPANY = "company";
    public static String RELATION_TYPE_MODEL = "model";


    public final static int PRODUCTION_OK = 4001;

    public final static String ETH_URL = "http://127.0.0.1:7545/";

    public static String ETH_TO_ADDRESS = null;
}
