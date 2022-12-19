/*抠图算法用的js  有背景证件照特用*/

const canvas_bg = document.getElementById('canvas_bg');
var load_time = 1
//   调用模型进行去除背景化处理
async function main() {
    const video = document.querySelector('video');
    const canvas = document.getElementById('canvas');
    
    const webcam = await tf.data.webcam(video);
    const model = await tf.loadGraphModel('/idphotosrc/model/model.json');

    let [r1i, r2i, r3i, r4i] = [tf.tensor(0.), tf.tensor(0.), tf.tensor(0.), tf.tensor(0.)];

    const downsample_ratio = tf.tensor(0.5);
    while (true) {
        await tf.nextFrame();
        const img = await webcam.capture();
        const src = tf.tidy(() => img.expandDims(0).div(255)); 
        const [fgr, pha, r1o, r2o, r3o, r4o] = await model.executeAsync(
            { src, r1i, r2i, r3i, r4i, downsample_ratio },
            ['fgr', 'pha', 'r1o', 'r2o', 'r3o', 'r4o']
        );
        drawMatte(fgr.clone(), pha.clone(), canvas);
        tf.dispose([img, src, fgr, pha, r1i, r2i, r3i, r4i]);
        [r1i, r2i, r3i, r4i] = [r1o, r2o, r3o, r4o];
    }
}

async function drawMatte(fgr, pha, canvas) {
    const rgba = tf.tidy(() => {
        const rgb = (fgr !== null) ?
            fgr.squeeze(0).mul(255).cast('int32') :
            tf.fill([pha.shape[1], pha.shape[2], 3], 255, 'int32');
        const a = (pha !== null) ?
            pha.squeeze(0).mul(255).cast('int32') :
            tf.fill([fgr.shape[1], fgr.shape[2], 1], 255, 'int32');
        return tf.concat([rgb, a], -1);
    });

    fgr && fgr.dispose();
    pha && pha.dispose();
    const [height, width] = rgba.shape.slice(0, 2);
    const pixelData = new Uint8ClampedArray(await rgba.data());
    const imageData = new ImageData(pixelData, width, height);
    canvas.width = width;
    canvas.height = height;
    var context = canvas.getContext("2d")
    context.putImageData(imageData, 0, 0);
    context.getImageData(0, 0, width, height)
    context.globalCompositeOperation = "destination-over"
    context.drawImage(canvas_bg, 0, 0)
    rgba.dispose();

    //添加个人代码,只生效一次就够了
    if(load_time==1){
        load_time+=1

        //用一个定时器当有的背景画布开始起效后再等2秒进入程序，解决黑屏前等待效果消失用户体感不好的问题
        var tmp_ms = 2
        var dsq = setInterval(function(){
            if(tmp_ms<0){
                $("#wait").remove()
                clearInterval(dsq)
            }else{
                tmp_ms--
            }
        },1000)

    }
}
