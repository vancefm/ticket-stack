import {createContext, useCallback, useContext, useState} from "react";
import {Alert} from "reactstrap";

const AlertContext = createContext()

export function AlertProvider(props){
    const [open, setOpen] = useState(false);
    const [message, setMessage] = useState();

    const handleClose = useCallback(() => {
        setOpen(false);
    }, [setOpen])

    const handleOpen = useCallback(message => {
        setMessage(message)
        setOpen(true)
    }, [setMessage, setOpen])

    return (
        <AlertContext.Provider value={[handleOpen, handleClose]}>
            {props.children}
            <Alert color="danger" isOpen={open} toggle={handleClose}>
                {message}
            </Alert>
        </AlertContext.Provider>
    )
}

export function useAlert() {
    const context = useContext(AlertContext)
    if(!context)
        throw new Error('`useAlert()` must be called inside an `AlertProvider` child.')

    return context
}