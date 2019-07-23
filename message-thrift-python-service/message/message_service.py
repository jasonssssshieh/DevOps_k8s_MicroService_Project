# coding: utf-8
from message.api import MessageService
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer


import smtplib
from email.mime.text import MIMEText
from email.header import Header

sender = "imoocd@163.com"
authCode = 'aA111111'

class MessageServiceHandler:
    def sendMobileMessage(self, mobile, message):
        print "sendMobileMessage, mobile:" + mobile + ", message:" + message;
        return True

    def sendEmailMessage(self, email, message):
        print "sendEmailMessage, email: " + email + ", message:" + message;
        messageObj = MIMEText(message,"plain", "utf-8")
        messageObj['From'] = sender
        messageObj['To'] = email
        messageObj['Subject'] = Header("IMOOC-Python测试邮件", 'utf-8')
        try:
            smtpObj = smtplib.SMTP('smpt.163.com')
            smtpObj.login(sender, authCode)
            smtpObj.sendmail(sender, [email], messageObj.as_string())
            print "send mail successfully"
            return True
        except smtplib.SMTPException, ex:
            print "send mail failed! "
            print ex
            return False


if __name__ == '__main__':
    handler = MessageServiceHandler()
    processor = MessageService.Processor(handler)#消息来了谁处理->processor处理
    transport = TSocket.TServerSocket("localhost", "9090")#开启一个Socket 监听本机上的9090端口
    tfactory = TTransport.TFramedTransportFactory()#传输方式: 帧传输
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()#传输协议: 二进制的传输协议

    #创建一个server
    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)
    print "python thrift server start."
    server.serve()
    print "python thrift server exit."
