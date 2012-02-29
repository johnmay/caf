package uk.ac.ebi.caf.component.factory;

import uk.ac.ebi.caf.component.CalloutDialog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

/**
 * ${Name}.java - 21.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ShapeFactory {

    public static Area getCalloutShape(Dimension dimension){

        int pad      = 7;
        int triWidth = 25;
        int nonPointPad = pad + triWidth;
        int halfPad     = nonPointPad / 2;
        int center   = dimension.width / 2;


        RoundRectangle2D rect = new RoundRectangle2D.Double(halfPad, halfPad,
                                                                 dimension.width  - nonPointPad, dimension.height - nonPointPad,
                                                                 halfPad, halfPad);


        int[] x = new int[]{
                center,
                center - triWidth,
                center + triWidth
        };
        int[] y = new int[]{
                dimension.height - pad,
                dimension.height - pad - triWidth,
                dimension.height - pad - triWidth};
        Polygon p = new Polygon(x, y, 3);

        Area composite = new Area(rect);
        composite.add(new Area(p));

        return composite;

    }

    public static BufferedImage getCalloutImage(Dimension d){

        BufferedImage img = new BufferedImage(d.width,
                                              d.height,
                                              BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fill(getCalloutShape(d));

        applyShadow(img, 8, Color.BLACK, 0.8f);
        g2.setColor(Color.WHITE);
        g2.fill(getCalloutShape(d));
        g2.dispose();

        return img;
        
    }
    
    
    private static void applyShadow(BufferedImage image,
                             int shadowSize,
                             Color shadowColor,
                             float shadowOpacity) {

        int dstWidth = image.getWidth();
        int dstHeight = image.getHeight();

        int left = (shadowSize - 1) >> 1;
        int right = shadowSize - left;
        int xStart = left;
        int xStop = dstWidth - right;
        int yStart = left;
        int yStop = dstHeight - right;

        int shadowRgb = shadowColor.getRGB() & 0x00FFFFFF;

        int[] aHistory = new int[shadowSize];
        int historyIdx = 0;

        int aSum;

        int[] dataBuffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int lastPixelOffset = right * dstWidth;
        float sumDivider = shadowOpacity / shadowSize;

        // horizontal pass

        for (int y = 0, bufferOffset = 0; y < dstHeight; y++, bufferOffset = y * dstWidth) {
            aSum = 0;
            historyIdx = 0;
            for (int x = 0; x < shadowSize; x++, bufferOffset++) {
                int a = dataBuffer[bufferOffset] >>> 24;
                aHistory[x] = a;
                aSum += a;
            }

            bufferOffset -= right;

            for (int x = xStart; x < xStop; x++, bufferOffset++) {
                int a = (int) (aSum * sumDivider);
                dataBuffer[bufferOffset] = a << 24 | shadowRgb;

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];

                // get the lastest pixel
                a = dataBuffer[bufferOffset + right] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;

                if (++historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }
        }

        // vertical pass
        for (int x = 0, bufferOffset = 0; x < dstWidth; x++, bufferOffset = x) {
            aSum = 0;
            historyIdx = 0;
            for (int y = 0; y < shadowSize; y++, bufferOffset += dstWidth) {
                int a = dataBuffer[bufferOffset] >>> 24;
                aHistory[y] = a;
                aSum += a;
            }

            bufferOffset -= lastPixelOffset;

            for (int y = yStart; y < yStop; y++, bufferOffset += dstWidth) {
                int a = (int) (aSum * sumDivider);
                dataBuffer[bufferOffset] = a << 24 | shadowRgb;

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];

                // get the lastest pixel
                a = dataBuffer[bufferOffset + lastPixelOffset] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;

                if (++historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }
        }
    }

}
