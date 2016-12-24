/*
Copyright (c) [2016] [epikosrest@gmail.com]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package metrics;

import com.yammer.metrics.core.*;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Created by nitina on 3/24/16.
 */
public  class Metrics {

    private Class<?> classToRegister;
    //Counter
    private Counter numberOfCharactersProcessedFromRequest;
    private Counter numberOfCharacterReturnedInResponse;
    //Meter
    private Meter reqeustMessages;
    private Meter responseMessages;
    //Timer
    private com.yammer.metrics.core.Timer responseTime;
    private LinkedList<String> historyOfReqeustQueries;
    TimerContext timerContext;



    public Metrics(Class<?> classToReg){
        this.classToRegister = classToReg;
        numberOfCharactersProcessedFromRequest = com.yammer.metrics.Metrics.newCounter(classToReg, "Total-Request-Byte-Processed");
        numberOfCharacterReturnedInResponse= com.yammer.metrics.Metrics.newCounter(classToReg, "Total-Response-Byte-Processed");

        reqeustMessages = com.yammer.metrics.Metrics.newMeter(classToReg,  "Number-Of-Request-Processed", "Received", TimeUnit.MILLISECONDS);
        responseMessages = com.yammer.metrics.Metrics.newMeter(classToReg,  "Number-Of-Response-Processed", "Send", TimeUnit.MILLISECONDS);



        responseTime = com.yammer.metrics.Metrics.newTimer(classToReg,  "Response-Time");
        historyOfReqeustQueries = new LinkedList<String>();

        {
            //Gauge
            com.yammer.metrics.Metrics.newGauge(classToReg, "lastQuery", new Gauge<String>() {

                @Override
                public String value() {
                    return historyOfReqeustQueries.getLast();
                }
            });

        }

    }

    public void startTimerContext(){
        timerContext = responseTime.time();
    }

    public void stopTimerContext(){
        if(timerContext != null){
            timerContext.stop();
        }
    }

    public void updateRequestMetrics(int bodySize,String path) {
        numberOfCharactersProcessedFromRequest.inc(bodySize);
        reqeustMessages.mark();
        historyOfReqeustQueries.addLast(path);
    }

    public void updateResponseMetrics(int bodySize, String path){
        numberOfCharacterReturnedInResponse.inc(bodySize);
        responseMessages.mark();
        //historyOfQueries.addLast(path);
    }
}
