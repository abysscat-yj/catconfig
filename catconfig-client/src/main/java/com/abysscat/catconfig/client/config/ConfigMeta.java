package com.abysscat.catconfig.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * config meta.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/5 17:55
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConfigMeta {

    String app;

    String env;

    String ns;

    String configServer;

    public String genKey() {
        return this.getApp() + "_" + this.getEnv() + "_" + this.getNs();
    }

    public String listPath() {
        return path("list");
    }

    public String versionPath() {
        return path("version");
    }

    private String path(String context) {
        return this.getConfigServer() + "/" + context + "?app=" + this.getApp()
                + "&env=" + this.getEnv() + "&ns=" + this.getNs();
    }

}
