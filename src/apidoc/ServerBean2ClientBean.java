package apidoc;

import utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class ServerBean2ClientBean {

    public static void main(String[] args) throws IOException {
        String srcPath = "/Users/lcy/Documents/code/SpringBootDemo/src/main/java/com/boredream/springbootdemo/entity";
        String tarPath = "/Users/lcy/Documents/code/LoveCookBook-Android/app/src/main/java/com/boredream/baseapplication/entity";

        for (File file : FileUtils.getAllFiles(srcPath)) {
            String fileContent = FileUtils.readToString(file, "UTF-8");
            StringBuilder sb = new StringBuilder();
            for (String line : fileContent.split("\n")) {
                if(line.contains("import io.swagger.")) {
                    continue;
                }
                if(line.contains("import lombok.")) {
                    continue;
                }
                if(line.contains("import com.baomidou.")) {
                    continue;
                }
                if(line.contains("import org.slf4j.")) {
                    continue;
                }
                if(line.contains(" * @since")) {
                    continue;
                }
                if(line.contains("@Data")) {
                    continue;
                }
                if(line.contains("@EqualsAndHashCode(callSuper = true)")) {
                    continue;
                }
                if(line.contains("private static final Logger log")) {
                    continue;
                }
                if(line.contains("serialVersionUID")) {
                    continue;
                }
                if(line.contains("@TableId")) {
                    continue;
                }
                if(line.contains("@TableField")) {
                    continue;
                }
                if(line.contains("@ApiModel(value=")) {
                    continue;
                }

                if(line.contains(" @ApiModelProperty(value = \"")) {
                    line = line.split("value = \"")[1];
                    line = "\t// " + line.substring(0, line.length() - 2);
                    if(line.endsWith("\"")) {
                        line = line.substring(0, line.length() - 1);
                    }
                }

                sb.append(line).append("\n");
            }
            String tarFilePath = file.getAbsolutePath().replace(srcPath, tarPath);

            String srcPackage = FileUtils.getPackageFromPath(file.getAbsolutePath(), "entity");
            String tarPackage = FileUtils.getPackageFromPath(tarFilePath, "entity");
            fileContent = sb.toString().replaceFirst(srcPackage, tarPackage);

            File tarFile = new File(tarFilePath);
            if(tarFile.exists()) {
                System.out.println("exist!\n=> " + tarFile.getAbsolutePath() + "\n");
                continue;
            }

            File dir = tarFile.getParentFile();
            if(!dir.exists()) {
                dir.mkdirs();
            }
            FileUtils.writeString2File(fileContent, tarFile, "UTF-8");
            System.out.println("convert " + file.getAbsolutePath() + " to \n=> " + tarFile.getAbsolutePath() + "\n");
        }

    }

}
