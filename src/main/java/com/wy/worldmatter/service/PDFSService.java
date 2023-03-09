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
     * 描述: 使用aspose-pdf技术，进行多pdf转word功能 <br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/8 <br/>
     * 参数: files-前端传来需要转换的pdf文件 <br/>
     * 返回值: 0-传入的文件集合为空,1-文件超过了10个,2-文件保存到服务器过程中出现意外,3-文件转换时发生错误,4-服务器线程池发生异常,5-任务完成并返回加密后的结果压缩文件路径 <br/>
     *       6-线程资源总数不足,在配置文件中调大 <br/>
     */
    public Map apPdfToDocx(MultipartFile[] files);

    /**
     * 描述: 使用spire技术，进行多pdf转word<br/>
     * 作者: wangyang <br/>
     * 创建时间: 2022/11/5 <br/>
     * 参数: files-前端传来需要转换的pdf文件 <br/>
     * 返回值: 0-传入的文件集合为空,1-文件超过了10个,2-文件保存到服务器过程中出现意外,3-文件转换时发生错误,4-服务器线程池发生异常,5-任务完成并返回加密后的结果压缩文件路径 <br/>
     *        6-线程资源总数不足,在配置文件中调大 <br/>
     */
    public Map srPdfToDocx(MultipartFile[] files);

}
