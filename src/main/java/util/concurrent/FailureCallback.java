package util.concurrent;

public interface FailureCallback {
	void onFailure(Throwable ex);
}