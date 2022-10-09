package egovframework.fileManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

@Controller
public class FileController {


    @Autowired
    EgovFileMngService egovFileMngService;

    @RequestMapping(value = "/cmm/fms/FileDown.do")
    public void cvplFileDownload(@RequestParam Map<String, Object> commandMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String atchFileId = (String) commandMap.get("atchFileId");
        String fileSn = (String) commandMap.get("fileSn");

        FileVO fileVO = new FileVO();
        fileVO.setAtchFileId(atchFileId);
        fileVO.setFileSn(fileSn);
        FileVO fvo = egovFileMngService.selectFileInf(fileVO);

        String fileStorePath = EgovProperties.getProperty("fileStorePath");

        File uFile = new File(fileStorePath + fvo.getFileStreCours(), fvo.getStreFileNm()+"."+fvo.getFileExtsn());
        int fSize = (int) uFile.length();

        if (fSize > 0) {
            String mimetype = "application/x-msdownload";

            //response.setBufferSize(fSize);	// OutOfMemeory 발생
            response.setContentType(mimetype);
            //response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fvo.getOrignlFileNm(), "utf-8") + "\"");
            setDisposition(fvo.getOrignlFileNm(), request, response);
            response.setContentLength(fSize);

            /*
             * FileCopyUtils.copy(in, response.getOutputStream());
             * in.close();
             * response.getOutputStream().flush();
             * response.getOutputStream().close();
             */
            BufferedInputStream in = null;
            BufferedOutputStream out = null;

            try {
                in = new BufferedInputStream(new FileInputStream(uFile));
                out = new BufferedOutputStream(response.getOutputStream());

                FileCopyUtils.copy(in, out);
                out.flush();
            } catch (IOException ex) {
                // 다음 Exception 무시 처리
                // Connection reset by peer: socket write error
                EgovBasicLogger.ignore("IO Exception", ex);
            } finally {
                EgovResourceCloseHelper.close(in, out);
            }

        } else {
            response.setContentType("application/x-msdownload");

            PrintWriter printwriter = response.getWriter();

            printwriter.println("<html>");
            printwriter.println("<br><br><br><h2>Could not get file name:<br>" + fvo.getOrignlFileNm() + "</h2>");
            printwriter.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
            printwriter.println("<br><br><br>&copy; webAccess");
            printwriter.println("</html>");

            printwriter.flush();
            printwriter.close();
        }

    }

    private void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String browser = getBrowser(request);

        String dispositionPrefix = "attachment; filename=";
        String encodedFilename = null;

        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Trident")) { // IE11 문자열 깨짐 방지
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else {
            throw new IOException("Not supported browser");
        }

        response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

        if ("Opera".equals(browser)) {
            response.setContentType("application/octet-stream;charset=UTF-8");
        }
    }

    private String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Trident") > -1) { // IE11 문자열 깨짐 방지
            return "Trident";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "Firefox";
    }

}
