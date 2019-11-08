package pers.li.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ���������ɹ�����
 */
public class BarCodeUtil {

    /**
     * generateCode ����code������Ӧ��һά��
     *
     * @param code   һά������
     * @param width  ͼƬ���
     * @param height ͼƬ�߶�
     */
    public static BufferedImage generateCode(String code, int width, int height) {
        //����λͼ����BitMatrix
        BitMatrix matrix = null;
        try {
            // ʹ��code_128��ʽ���б�������100*25��������
            MultiFormatWriter writer = new MultiFormatWriter();

            matrix = writer.encode(code, BarcodeFormat.CODE_128, width, height, null);
            //matrix = writer.encode(code,BarcodeFormat.EAN_13, width, height, null);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
        return bufferedImage;
    }

    /**
     * readCode ��ȡһ��һά��ͼƬ
     *
     * @param file һά��ͼƬ����
     */
    public static String readCode(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            if (image == null) {
                return null;
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "GBK");
            hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            Result result = new MultiFormatReader().decode(bitmap, hints);
            System.out.println("����������: " + result.getText());
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        BufferedImage bufferedImage = generateCode("123456789012", 500, 250);
        //��λͼ����BitMatrix����ΪͼƬ
        try (FileOutputStream outStream = new FileOutputStream(new File("1dcode.png"))) {
            //һά�뱣��Ϊ�ļ�
            ImageIO.write(bufferedImage, "png", outStream);
            //ͨ������д�������
            //ImageIO.write(buffImg, "jpg", response.getOutputStream());
            outStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //һά�����ݽ���
        String s = readCode(new File("1dcode.png"));

    }
}
