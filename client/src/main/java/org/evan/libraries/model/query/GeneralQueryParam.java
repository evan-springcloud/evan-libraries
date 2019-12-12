package org.evan.libraries.model.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用的分页参数
 *
 * @author Evan.Shen
 * @since 2019-12-12
 */
public class GeneralQueryParam extends AbstractQueryParam implements Serializable, QueryParam {
    private static final long serialVersionUID = 2941998018140190485L;

    private Map<String, Object> params;

    /**
     *
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /***/
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
