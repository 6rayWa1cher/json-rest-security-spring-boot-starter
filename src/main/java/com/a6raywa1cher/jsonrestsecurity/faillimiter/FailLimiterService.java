package com.a6raywa1cher.jsonrestsecurity.faillimiter;

/**
 * Holds fail stats for all clients.
 *
 * @see LoadingCacheFailLimiterService
 */
public interface FailLimiterService {
	/**
	 * Appends fail stats for the client
	 *
	 * @param key client remote address (ip)
	 */
	void actionFailed(String key);

	/**
	 * Checks if the client is banned
	 *
	 * @param key client remote address (ip)
	 * @return is client banned
	 */
	boolean isBlocked(String key);
}
