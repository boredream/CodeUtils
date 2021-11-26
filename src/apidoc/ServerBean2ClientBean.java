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
                if(line.contains("import io.swagger.annotations.ApiModelProperty;")) {
                    continue;
                }
                if(line.contains("import lombok.Data;")) {
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
                if(line.contains("serialVersionUID")) {
                    continue;
                }
                if(line.contains("@ApiModel(value=")) {
                    continue;
                }

                if(line.contains(" @ApiModelProperty(value = \"")) {
                    line = line.split("value = \"")[1];
                    line = "\t// " + line.substring(0, line.length() - 2);
                }

                sb.append(line).append("\n");
            }
            tarPath = file.getAbsolutePath().replace(srcPath, tarPath);

            String srcPackage = FileUtils.getPackageFromPath(file.getAbsolutePath(), "entity");
            String tarPackage = FileUtils.getPackageFromPath(tarPath, "entity");
            fileContent = sb.toString().replaceFirst(srcPackage, tarPackage);

            File tarFile = new File(tarPath);
            File dir = tarFile.getParentFile();
            if(!dir.exists()) {
                dir.mkdirs();
            }
            FileUtils.writeString2File(fileContent, tarFile, "UTF-8");

            System.out.println("convert " + file.getAbsolutePath() + " to \n=> " + tarFile.getAbsolutePath() + "\n");

            break;
        }

    }

}
