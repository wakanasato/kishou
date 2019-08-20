package com.esri.geoevent.adapter.kishou;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GeoEventException extends Exception {
    final Log log = LogFactory.getLog(KishouInboundAdapter.class);

    GeoEventException(String msg) {
        log.error(msg);
    }
}
