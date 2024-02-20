package util.concurrent;

public interface SuccessCallback<T> {
	void onSuccess(T result);
}
