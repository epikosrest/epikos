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
    private Counter numberOfSendCharacters;
    //Meter
    private Meter sendMessages;
    //Timer
    private com.yammer.metrics.core.Timer responseTime;
    private LinkedList<String> historyOfQueries;
    TimerContext timerContext;



    public Metrics(Class<?> classToReg){
        this.classToRegister = classToReg;
        numberOfSendCharacters = com.yammer.metrics.Metrics.newCounter(classToReg, "Total-Byte-Processed");
        sendMessages = com.yammer.metrics.Metrics.newMeter(classToReg,  "Number-Of-Request-Proccessed", "Send", TimeUnit.SECONDS);
        responseTime = com.yammer.metrics.Metrics.newTimer(classToReg,  "Response-Time");
        historyOfQueries = new LinkedList<String>();

        {
            //Gauge
            com.yammer.metrics.Metrics.newGauge(classToReg, "lastQuery", new Gauge<String>() {

                @Override
                public String value() {
                    return historyOfQueries.getLast();
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

    public void updateMetrics(String message) {
        numberOfSendCharacters.inc(message.length());
        sendMessages.mark();
        historyOfQueries.addLast(message);
    }
}
