package edu.cmu.sv.ws.ssnoc.common.utils;

import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Memory;
import edu.cmu.sv.ws.ssnoc.dto.User;

/**
 * This is a utility class used to convert PO (Persistent Objects) and View
 * Objects into DTO (Data Transfer Objects) objects, and vice versa. <br/>
 * Rather than having the conversion code in all classes in the rest package,
 * they are maintained here for code re-usability and modularity.
 * 
 */
public class ConverterUtils {
	/**
	 * Convert UserPO to User DTO object.
	 * 
	 * @param po
	 *            - User PO object
	 * 
	 * @return - User DTO Object
	 */
	public static final User convert(UserPO po) {
		if (po == null) {
			return null;
		}

		User dto = new User();
		dto.setUserName(po.getUserName());

		dto.setProfession(po.getProfession());
		dto.setRole(po.getRole());
		dto.setAccountStatus(po.getAccountStatus());
		dto.setLatitude(po.getLatitude());
		dto.setLongitude(po.getLongitude());

		return dto;
	}

	/**
	 * Convert User DTO to UserPO object
	 * 
	 * @param dto
	 *            - User DTO object
	 * 
	 * @return - UserPO object
	 */
	public static final UserPO convert(User dto) {
		if (dto == null) {
			return null;
		}

		UserPO po = new UserPO();
		po.setUserName(dto.getUserName());
		po.setPassword(dto.getPassword());
        po.setProfession(dto.getProfession());
        po.setLatitude(dto.getLatitude());
        po.setLongitude(dto.getLongitude());
		return po;
	}
	
    public static final Memory convert(MemoryPO memdto){
        if(memdto == null){
            return null;
        }
    Memory memstats = new Memory();
    memstats.setUsedNonVolatile(memdto.getUsedNonVolatile());
    memstats.setFreeNonVolatile(memdto.getFreeNonVolatile());
    memstats.setUsedVolatile(memdto.getUsedVolatile());
    memstats.setFreeVolatile(memdto.getFreeVolatile());
    memstats.setCreatedAt(memdto.getCreatedAt());
//    memstats.setMinutes(memdto.getMinutes());

    return memstats;
}
	
	
}
