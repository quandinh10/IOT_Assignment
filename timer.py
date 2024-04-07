'''
    Software timer
'''
class Timer():
    def __init__(self, duration):
        self.timer_counter = duration
        self.timer_flag = 0
    
    def setTimer(self, duration):
        self.timer_counter = duration
        self.timer_flag = 0

    def timerRun(self):
        if (self.timer_counter > 0):
            self.timer_counter -= 1
            if (self.timer_counter <= 0):
                self.timer_flag = 1
