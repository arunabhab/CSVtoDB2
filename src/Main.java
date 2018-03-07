import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
	private static int NO_OF_CORES=2;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.print("Enter Number of Thread:");
		Scanner sc=new Scanner(System.in);
		int numOfThread=sc.nextInt();
		Instant starttime = Instant.now();
		String fileName="doc/cu0.xlsx";
		File src=new File(fileName);
		try {
			FileInputStream fis=new FileInputStream(src);
			XSSFWorkbook wb=new XSSFWorkbook(fis);
			XSSFSheet sheet1=wb.getSheetAt(0);			
			int numOfRecord=sheet1.getPhysicalNumberOfRows()-1;
			int numPages=numOfRecord/numOfThread;
			int remainingRecord=numOfRecord-numOfThread*numPages;
			ExecutorService execService=Executors.newFixedThreadPool(NO_OF_CORES);
			int start,end;
			int i;
			for(i=0;i<numOfThread;i++)
			{
				start=i*numPages+1;
				end=start+numPages-1;
				TestMultiNaming1 thread=new TestMultiNaming1(sheet1);
				thread.setIndex(start, end,i);
				execService.execute(thread);
			}
			while(remainingRecord!=0)
			{
				start=numOfRecord-remainingRecord+1;
				end=numOfRecord-remainingRecord+1;
				TestMultiNaming1 thread=new TestMultiNaming1(sheet1);
				thread.setIndex(start, end,++i);
				execService.execute(thread);
				remainingRecord--;
			}
			execService.shutdown();  
		    while (!execService.isTerminated()) {   }  
			wb.close();
			sc.close();
			Instant endtime = Instant.now();
			System.out.println(Duration.between(starttime, endtime));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}