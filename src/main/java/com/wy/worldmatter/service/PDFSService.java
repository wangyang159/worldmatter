package com.wy.worldmatter.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 作者: wangyang <br/>
 * 创建时间: 2022/11/8 <br/>
 * 描述: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;PDFSService
 * 多PDF转Word功能
 */
public interface PDFSService {

    /**
     * 描述: 保存文件的方法 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/8 <br/>
     * 参数:  <br/>
     * 返回值: 返回保存后的原始文件名称  <br/>
     */
    public String saveFile(MultipartFile file) throws IOException;

    /**
     * 描述: 使用aspose-pdf技术，进行多pdf转word功能 <br/>
     *   1、无水印 <br/>
     *   2、结果文字成段落格式，但是如果原pdf中的文字也是图片则结果也是整个的图，例如WPS-PDF的文字就是图片 <br/>
     *   3、正常的文字格式加图片格式的PDF转换，结果可能存在多张图片合成了一张大图，这点需要用户自己裁剪一下 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/8 <br/>
     * 参数: files前端传来的包含PDF的数组 <br/>
     * 返回值: 0-传入的文件集合为空,1-文件超过了10个,2-文件保存到服务器过程中出现意外,3-文件转换时发生错误,4-服务器线程池发生异常,5-任务完成并返回加密后的结果压缩文件路径 <br/>
     */
    public Map apPdfToDocx(MultipartFile[] files);

    /**
     * 描述: 使用spire技术，进行多pdf转word<br/>
     *   1、有文本框水印，不过可以删除 <br/>
     *   2、对应WPD-PDF可以转换成单个的文字图片 <br/>
     *   3、对于正常的文字格式和图片格式PDF，结果是在段落的格式上每一行是一个文本框，并且基本不出现多张图片识别为一张的情况
     *      不过图文一起解析时结果中的英文字体被所用技术定死为了Arial-OKITQ，在想办法解决，
     *      单独转文本PDF不会出现英文字体的问题<br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/5 <br/>
     * 参数: 文件 <br/>
     * 返回值: 0-传入的文件集合为空,1-文件超过了10个,2-文件保存到服务器过程中出现意外,3-文件转换时发生错误,4-服务器线程池发生异常,5-任务完成并返回加密后的结果压缩文件路径 <br/>
     */
    public Map srPdfToDocx(MultipartFile[] files);

}
