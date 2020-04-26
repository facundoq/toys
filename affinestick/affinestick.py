

class KeyScaler:

    def __init__(self,key_down,key_up,initial_value:float,min_value:float,max_value:float,delta:float,loop=False):
        super().__init__()
        self.key_down=key_down
        self.key_up=key_up
        self.initial_value=value
        self.value=initial_value
        self.min_value=min_value
        self.max_value=max_value
        self.delta=delta
        self.loop=False
    
    def update(self):
        if pygame.key.get_pressed()[self.key_down]:
            self.value+=self.delta
        if pygame.key.get_pressed()[self.key_down]:
            self.value-=self.delta

        if pygame.key.get_pressed()[self.key_down] and pygame.key.get_pressed()[self.key_up]:
            self.value=self.initial_value
        if self.loop:
            if self.value>self.max_value:
                self.value=self.min_value
            if self.value<self.min_value:
                self.value=self.max_value
        else:
            self.value=min(self.value,self.max_value)
            self.value=max(self.value,self.min_value)
