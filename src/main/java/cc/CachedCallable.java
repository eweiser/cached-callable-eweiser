package cc;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A wrapper around a Callable object which ensures that the Callable is only called once.
 *
 * @param <V> the return type of the delegate
 */
public class CachedCallable<V> implements Callable<V> {

    private final Callable<V> delegate;
    private V result;
    private boolean hasCached;
    private Exception caughtException;

    public CachedCallable(final Callable<V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public synchronized V call() throws Exception {
        if (hasCached) {
            if (caughtException != null) {
                throw caughtException;
            } else {
                return result;
            }
        } else {
            hasCached = true;
            try {
                result = delegate.call();
                return result;
            } catch (Exception e) {
                caughtException = e;
                throw e;
            }
        }
    }

}
