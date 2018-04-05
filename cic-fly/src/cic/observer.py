import weakref
import types

##
# The Observer Pattern in Python
#
# Design goals:
# 1. An Observer should be able to observe multiple Observables.
# 2. An Observer should be able to tell an Observable what kinds of
# events it is interested in observing.
# 3. When an Observer is deleted, all Observables that it is observing
# should be notified to remove that Observer.
# 4. When an Observer is notified of an event,
# it should be able to tell which Observable is calling it,
# and which event occured.
##

##
# The abstract Observable Class.
#

class Observable(object):
    
    def __init__(self):
        # A WeakKeyDictionary is one where, if the object used as the key
        # gets deleted, automatically removes that key from the
        # dictionary.  Thus, any Observers which get deleted will be
        # automatically removed from the observers dictionary, thus having
        # two effects:
        # We won't have references to zombie objects in the dictionary, and
        # We won't have zombie objects, because the reference in this
        # dictionary won't stay around, and so won't keep the deleted object
        # alive.
        self._observers = {}

    ##
    # Add an observer to this Observable's notification list.
    # @param observer The Observer to add.
    # @param cbname The name (as a string) of the Observer's
    # method to call for an event notification, or None for the default
    # "update" method.
    # @param events The events the Observer is interested in being
    # notified about.  None means all events.

    def subscribe(self, observer, event=None,callback=None):
        if event is not None and type(event) not in (types.TupleType, 
                types.ListType):
            if (not event in self._observers):
                self._observers[event]={}
            self._observers[event][observer] = callback

    ##
    # Remove an observer from this Observable's list of observers.
    # Note that this function is not strictly required, because when a
    # registered Observer is deleted, the weakref mechanism will cause
    # it to be removed from the notification list.
    # @param observer the Observer to remove.

    def unsubscribe(self, observer,event):
        if event in self._observers:
            if observer in self._observers[event]:
                del self._observers[event][observer]

    ##
    # Notify all currently-registered Observers.
    # @param event The event to notify the Observers about.  None
    # means no specific event.
    # @param msg A reference to any data object that should be passed
    # to the Observers, or None.

    def notify(self, event=None, msg=None):
        if event in self._observers:
            event_observers= self._observers[event]
            for observer, callback in event_observers.items():
                callback(event, msg)


##
# The abstract Observer Class
# This class is a mix-in to add Observable registration methods to
# a concrete Observer class.  It is not strictly required.
#

class Observer(object):

    ##
    # @param observable The Observable to observe.
    # @param The events this Observer is interested in being
    # notified about.  This should be a tuple or list of events.

    def __init__(self, observable=None, cbname=None, events=None):
        if (observable is not None):
            observable.subscribe(self, cbname, events)

    ##
    # Inform an Observable that you would like to be notified when
    # an interesting event occurs in the Observable.
    # @param observable The Observable this Observer would like
    # to observe.
    # @param cbname The name (as a string) of the Observer's
    # method to call for an event notification, or None for the default
    # "update" method.
    # @param events A tuple or list of events this Observer would like
    # to be notified of by the Observer, or None if it would like to
    # be notified of all events.

    def subscribe_to(self, observable, cbname=None, events=None):
        assert observable is not None, "Observable is None"
        observable.suscribe(self, cbname, events)

    ##
    # Inform an observable     that this Observer is no longer interested in it.
    # Note that this function is not strictly required, because when a
    # registered Observer is deleted, the weakref mechanism will cause
    # it to be removed from the Observable's notification list.
    # Use this function when you want to unsubscribe an Observer
    # without deleting it.
    # @param observable The Observable that this Observer no longer wants
    # to observe.

    def unsubscribe_from(self, observable):
        assert observable is not None, "Observable is None"
        observable.unsuscribe(self)