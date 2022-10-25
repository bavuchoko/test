<%--
  Created by IntelliJ IDEA.
  User: pjs
  Date: 2022-10-08
  Time: 오전 11:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="/css/main.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="http://code.jquery.com/ui/1.8.18/themes/base/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
    <title>test</title>
</head>
<body>
<div id="mloader"><img id="loading-image" src="/images/common/loader.gif" alt="Loading..." /></div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://mozilla.github.io/pdf.js/build/pdf.js"></script>
<script>


    //파일선택
    function  uploadFile(obj,fdx){
        validate = true;
        isPdf = false;
        var maxSize = 10 * 1024 * 1024;
        var fileSize = obj.files[0].size;
        var idx = obj.dataset.index;
        if (fileSize > maxSize) {
            validate = false;
            alert("첨부파일 사이즈는 10MB 이내로 등록 가능합니다.");
            obj.value='';
            return false;
        } else {
            fileExtCheck(obj)
            if(validate) readURL(obj, idx, isPdf);
        }
        var filename ="";
        if(window.FileReader){
            filename  = obj.files[0].name;
        }
        else {  // old IE
            filename = $(this).val().split('/').pop().split('\\').pop();  // 파일명만 추출
        }
        $("#file-name"+idx).val(filename)
        $("#fileSn_"+idx).val("")
    };

    //미리보기
    function readURL(input, idx) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                if(!isPdf){
                    $("#preImage_"+idx).attr('src', e.target.result);
                }else{
                    $("#preImage_"+idx).attr('src', '/images/common/pdf.png');
                }
                    $("#preImage_"+idx).attr('data-format', isPdf);
            }
            reader.readAsDataURL(input.files[0]);
        }
    }

    //확장자 체크
    function fileExtCheck(obj){

        var docName = obj.value.substring(obj.value.lastIndexOf("\\")+1);
        var prefixList = ['exe','cgi', 'pl', 'plx', 'html', 'htm', 'xhtml', 'php', 'php3', 'php4', 'asp', 'asa', 'jsp', 'jsa', 'java', 'htaccess'];
        var fielExt = getFileExtension(docName);
        isPdf = getFileExtension(docName)== 'pdf' ? true : false ;
        for (var i = 0; i < prefixList.length; i++){
            if (fielExt == prefixList[i]) {
                validate = false;
                alert("확장자가  "+fielExt+"인 파일은 첨부할 수 없습니다.");
                obj.value='';
                return false;
            }
        }
        return true;
    }

    //확장자명 가져오기
    function getFileExtension( fileExtName ){
        var lastIndex = -1;
        lastIndex = fileExtName.lastIndexOf('.');
        var extension = "";
        if ( lastIndex != -1 ){
            extension = fileExtName.substring( lastIndex+1, fileExtName.len );
        } else {
            extension = "";
        }
        return extension;
    }


    //파일 뷰팝업 이미지 사이즈만큼
    function viewFile(url, isPdf){
        if(isPdf =="true"){
            view_img(url)
        }else {
            view_img(url) ;
        }
    }

    //이미지
    function view_img(url){
        var img=new Image();
        img.src=url;
        img.onload = function (){
            var img_width=img.width;
            var win_width=img.width+25;
            var img_height=img.height;
            var win=img.height+30;
            var OpenWindow=window.open('','_blank', 'width='+img_width+', height='+img_height+', menubars=no, scrollbars=auto');
            OpenWindow.document.write("<style>body{margin:0px;}</style><img src='"+url+"' width='"+win_width+"'>");
        }
    }
    function viewFileViewPage(id, extension){
        var url = "";

        if(extension != "pdf"){
            url = "<c:url value='/cmm/fms/getImage.do'/>?atchFileId="+id+"&fileSn=0"
            view_img(url);
            console.log(url)
        }else{
            url = "<c:url value='/cmm/fms/getPdf.do'/>?atchFileId="+id+"&fileSn=0"
            viewPdfFile(url);
        }
    }

    //파일 뷰팝업 이미지 사이즈만큼
    function viewPdfFile(url){
        var OpenWindow=window.open('','_blank', 'menubars=no, scrollbars=auto');
        OpenWindow.document.write("<style>body{margin:0px;}</style><iframe width='100%' height='100%' src='"+url+"'></iframe>");
    }


</script>