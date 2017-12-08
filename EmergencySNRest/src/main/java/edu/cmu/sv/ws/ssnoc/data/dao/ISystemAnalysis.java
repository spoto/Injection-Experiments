package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;

public interface ISystemAnalysis {
	
	/**
	 * Get private conversations for the past 'n' hours
	 * @param n
	 * @return
	 */
	List<MessageDetailPO> getPrivateChatsPast(int n);
	
	/**
	 * Get user cluster for each user, update set list
	 * @param set
	 * @param message
	 * @return
	 */
	List<String> setCloseUserClusters(List<Long> set, List<MessageDetailPO> messages, Long fromId, UserDAOImpl userDAO);
}
