package edu.cmu.sv.ws.ssnoc.rest;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.FileStore;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.sun.management.OperatingSystemMXBean;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMemoryDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;
import edu.cmu.sv.ws.ssnoc.dto.Memory;

@SuppressWarnings("restriction")
@Path("/memory")
public class MeasureMemoryService extends BaseService{
	public static Timer memoryTimer;
	public static TimerTask memoryTask;
	public static boolean stop = true;
	int minutes = 0;
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/start")
	public Response startMemoryMeasurement() {
		if(stop){
			stop = false;
			memoryTimer = new Timer();
			memoryTask = new TimerTask(){
				IMemoryDAO mdao = DAOFactory.getInstance().getMemoryDAO() ;
				MemoryPO memDetails = new MemoryPO();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				public void run(){
					if(stop ==false){
						OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	
						long maxMemory = bean.getTotalPhysicalMemorySize()/1024;
						long freeVMemory = bean.getFreePhysicalMemorySize()/1024;
	
						long usedVMemory = maxMemory-freeVMemory;
	
						long freeSpace = 0;
						long usedSpace = 0;
	
						for(java.nio.file.Path root : FileSystems.getDefault().getRootDirectories())
						{
							try{
								FileStore store = Files.getFileStore(root);
								freeSpace = store.getUnallocatedSpace()/1024;
								usedSpace = ((store.getTotalSpace()/1024)-(store.getUnallocatedSpace()/1024));
							}catch (FileSystemException e){
								Log.trace(e.toString());
							}catch (IOException e){
								Log.trace(e.toString());
							}
	
							Log.trace("Time elapsed: " + minutes);
							minutes++;
	
						}
						memDetails.setUsedVolatile(usedVMemory);
						memDetails.setFreeVolatile(freeVMemory);
						memDetails.setUsedNonVolatile(usedSpace);
						memDetails.setFreeNonVolatile(freeSpace);
						Calendar calendar = Calendar.getInstance();
						memDetails.setCreatedAt(df.format(calendar.getTime()));
						//		                    memDetails.setMinutes(minutes);
						mdao.insertMemoryStats(memDetails);
					}
					//	        	   else{
					//	        		   memoryTimer.cancel();
					//	        	   }
					//	               number = number + 1;
				}
			};
			//	      Timer memoryTimer = new Timer();
			memoryTimer.scheduleAtFixedRate(memoryTask, 0, 60000);
		}
		return ok();
	}
	@POST
	@Path("/stop")
	public void stopMemoryMeasurement(){
		stop = true;
		minutes = 0;
		memoryTimer.cancel();
		memoryTask.cancel();
		Log.trace("Memory measurement terminated");
	}

	@DELETE
	public void deleteMemoryCrumbData(){
		IMemoryDAO mdao = DAOFactory.getInstance().getMemoryDAO();
		mdao.deleteMemoryCrumbData();
		Log.trace("Memory Crumb cleared");
	}

	@GET
	@Path("/getMemory")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@XmlElementWrapper(name = "memorystats")

	public List<Memory> loadMemoryStats(){
		Calendar calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String toDate = df.format(calendar.getTime());
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		String fromDate = df.format(calendar.getTime());
		Log.trace(toDate,fromDate);
		List<Memory> memorystats = null;
		try{
			IMemoryDAO mdao = DAOFactory.getInstance().getMemoryDAO();
			List<MemoryPO> memPOs = mdao.getMemoryStats(toDate,fromDate);

			memorystats = new ArrayList<Memory>();
			for(MemoryPO memPO : memPOs){
				Memory memdto = ConverterUtils.convert(memPO);
				memorystats.add(memdto);
			}
		}catch (Exception e){
			handleException(e);
		}finally {
			Log.exit(memorystats);
		}
		return memorystats;
	}


	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@XmlElementWrapper(name = "memorystats")
	@Path("/interval/{timeWindowsInHours}")
	public List<Memory> loadMemoryStatusInInterval(@PathParam("timeInterval") int timeInterval){
		Calendar calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String toDate = df.format(calendar.getTime());
		calendar.add(Calendar.HOUR_OF_DAY, -timeInterval);
		String fromDate = df.format(calendar.getTime());
		Log.trace(toDate,fromDate);
		List<Memory> memorystats = null;
		try{
			IMemoryDAO mdao = DAOFactory.getInstance().getMemoryDAO();
			List<MemoryPO> memPOs = mdao.getMemoryStats(toDate,fromDate );

			memorystats = new ArrayList<Memory>();
			for(MemoryPO memPO : memPOs){
				Memory memdto = ConverterUtils.convert(memPO);
				memorystats.add(memdto);
			}
		}catch (Exception e){
			handleException(e);
		}finally {
			Log.exit(memorystats);
		}
		return memorystats;
	}

}