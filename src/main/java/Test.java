import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @ClassName: Test
 * @Description:
 * @Author: lin
 * @Date: 2020/4/20 11:17
 * History:
 * @<version> 1.0
 */
public class Test {
    /**
     * 将一些 文件压缩到 jar里面去，然后在遍历出来
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        CodeSource codeSource = Test.class.getProtectionDomain().getCodeSource();
        if(null != codeSource){
            URL jar = codeSource.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            while (true){
                ZipEntry entry = zip.getNextEntry();
                if(entry == null){
                    break;
                }
                System.out.println(entry.getName());
            }
        }else{
            System.out.println("no data");
        }
    }

}
