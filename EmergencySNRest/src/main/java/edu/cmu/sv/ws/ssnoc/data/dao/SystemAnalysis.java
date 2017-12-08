package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

public class SystemAnalysis extends BaseDAOImpl implements ISystemAnalysis {
	@Override
	public List<MessageDetailPO> getPrivateChatsPast(int n) {
		java.util.Date date= new java.util.Date();
		Long dur = new Long(n * 60 * 60 * 1000);
		Timestamp timeStmp = new Timestamp(date.getTime());
		timeStmp.setTime(timeStmp.getTime() - dur);

		PreparedStatement stmt;	ResultSet rs;
		List<MessageDetailPO> messages = null;
		try {
			Connection conn = getConnection();
			if(n > 0){
				stmt = conn.prepareStatement(SQL.FIND_CHAT_MESSAGES_BY_DUR);
				stmt.setTimestamp(1, timeStmp);
			}
			else
				stmt = conn.prepareStatement(SQL.FIND_ALL_CHAT_MESSAGES);
			messages = new ArrayList<MessageDetailPO>();
			rs = stmt.executeQuery();
			while (rs.next()) {
				MessageDetailPO mpo = new MessageDetailPO();
				mpo.setFrom_userId(rs.getLong(1));
				mpo.setTo_userId(rs.getLong(2));
				messages.add(mpo);				
			}
			closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messages;
	}

	@Override
	public List<String> setCloseUserClusters(List<Long> set,
			List<MessageDetailPO> messages, Long fromId, UserDAOImpl userDao) {

		// TODO remove Ids from set of people who have chatted
		if(messages != null){
			for(MessageDetailPO message : messages){
				if(fromId.equals(message.getFrom_userId())){
					if(message.getFrom_userId() != message.getTo_userId()){
						set.remove(message.getTo_userId());
						continue;
					}
				}
				else if (fromId.equals(message.getTo_userId())){
					if(message.getFrom_userId() != message.getTo_userId()){
						set.remove(message.getFrom_userId());
						continue;
					}
				}
				else
					continue;
			}
		}

		//		Get names of users in clusters
		UserPO user = new UserPO();
		UserPO from_user = new UserPO();
		List<String> clusterList = new ArrayList<String>();
		from_user = userDao.findById(fromId);
		clusterList.add(from_user.getUserName());

		for(Long idCluster : set){
			if(idCluster.compareTo(fromId) == 0)
				continue;
			user = userDao.findById(idCluster);
			clusterList.add(user.getUserName());
		}
		return clusterList;
	}
}
