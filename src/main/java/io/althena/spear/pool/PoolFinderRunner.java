package io.althena.spear.pool;

import com.squareup.okhttp.OkHttpClient;
import io.althena.spear.model.Asset;
import io.althena.spear.model.BasePool;
import java.util.concurrent.CountDownLatch;

/**
 * description
 *
 * @author chongyu.yuan
 * @since 2022/2/24
 */
public abstract class PoolFinderRunner {

    public abstract BasePool execute(Asset assetA, Asset assetB);

    public BasePool executeParallel(Asset assetA, Asset assetB, CountDownLatch latch) {
        try {
            return execute(assetA, assetB);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (latch != null) {
                latch.countDown();
            }
        }
        return null;
    }
}
