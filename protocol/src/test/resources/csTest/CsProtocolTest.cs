using System;
using System.IO;
using CsProtocol;
using CsProtocol.Buffer;
using NUnit.Framework;

namespace Test.Editor.Net
{
    public class CsProtocolTest
    {
        [Test]
        public void ComplexObjectTest()
        {
            ProtocolManager.InitProtocol();
            // 获取复杂对象的字节流
            var complexObjectBytes = File.ReadAllBytes("D:\\zfoo\\protocol\\src\\test\\resources\\ComplexObject.bytes");
            var buffer = ByteBuffer.ValueOf();
            buffer.WriteBytes(complexObjectBytes);
            var packet = ProtocolManager.Read(buffer);

            var newBuffer = ByteBuffer.ValueOf();
            ProtocolManager.Write(newBuffer, packet);
            var bytes = newBuffer.ToBytes();

            // set和map是无序的，所以有的时候输入和输出的字节流有可能不一致，但是长度一定是一致的
            AssertEquals(complexObjectBytes, bytes);
        }


        [Test]
        public void ByteBufferTest()
        {
            byteTest();
            bytesTest();
            shortTest();
            intTest();
            longTest();
            floatTest();
            doubleTest();
            charTest();
            stringTest();
        }


        public void byteTest()
        {
            byte value = 9;
            ByteBuffer writerByteBuffer = ByteBuffer.ValueOf();
            writerByteBuffer.WriteByte(value);
            byte[] bytes = writerByteBuffer.ToBytes();

            ByteBuffer readerByteBuffer = ByteBuffer.ValueOf();
            readerByteBuffer.WriteBytes(bytes);
            byte readValue = readerByteBuffer.ReadByte();
            AssertEquals(value, readValue);
        }

        public void bytesTest()
        {
            var value = new byte[] {1, 2, 3};
            ByteBuffer writerByteBuffer = ByteBuffer.ValueOf();
            writerByteBuffer.WriteBytes(value);
            byte[] bytes = writerByteBuffer.ToBytes();

            ByteBuffer readerByteBuffer = ByteBuffer.ValueOf();
            readerByteBuffer.WriteBytes(bytes);
            var readValue = readerByteBuffer.ReadBytes(3);
            AssertEquals<byte>(value, readValue);
        }

        public void shortTest()
        {
            short value = 9999;
            ByteBuffer writerByteBuffer = ByteBuffer.ValueOf();
            writerByteBuffer.WriteShort(value);
            byte[] bytes = writerByteBuffer.ToBytes();

            ByteBuffer readerByteBuffer = ByteBuffer.ValueOf();
            readerByteBuffer.WriteBytes(bytes);
            short readValue = readerByteBuffer.ReadShort();
            AssertEquals(value, readValue);
        }

        public void intTest()
        {
            int value = 99999999;
            ByteBuffer writerByteBuffer = ByteBuffer.ValueOf();
            writerByteBuffer.WriteInt(value);
            byte[] bytes = writerByteBuffer.ToBytes();

            ByteBuffer readerByteBuffer = ByteBuffer.ValueOf();
            readerByteBuffer.WriteBytes(bytes);
            int readValue = readerByteBuffer.ReadInt();
            AssertEquals(value, readValue);
        }

        public void longTest()
        {
            long value = 9999999999999999L;
            ByteBuffer writerByteBuffer = ByteBuffer.ValueOf();
            writerByteBuffer.WriteLong(value);
            byte[] bytes = writerByteBuffer.ToBytes();

            ByteBuffer readerByteBuffer = ByteBuffer.ValueOf();
            readerByteBuffer.WriteBytes(bytes);
            long readValue = readerByteBuffer.ReadLong();
            AssertEquals(value, readValue);
        }

        public void floatTest()
        {
            float value = 999999.56F;
            ByteBuffer writerByteBuffer = ByteBuffer.ValueOf();
            writerByteBuffer.WriteFloat(value);
            byte[] bytes = writerByteBuffer.ToBytes();

            ByteBuffer readerByteBuffer = ByteBuffer.ValueOf();
            readerByteBuffer.WriteBytes(bytes);
            float readValue = readerByteBuffer.ReadFloat();
            AssertEquals(value, readValue);
        }

        public void doubleTest()
        {
            double value = 999999.56;
            ByteBuffer writerByteBuffer = ByteBuffer.ValueOf();
            writerByteBuffer.WriteDouble(value);
            byte[] bytes = writerByteBuffer.ToBytes();

            ByteBuffer readerByteBuffer = ByteBuffer.ValueOf();
            readerByteBuffer.WriteBytes(bytes);
            double readValue = readerByteBuffer.ReadDouble();
            AssertEquals(value, readValue);
        }

        public void charTest()
        {
            char value = 'a';
            ByteBuffer writerByteBuffer = ByteBuffer.ValueOf();
            writerByteBuffer.WriteChar(value);
            byte[] bytes = writerByteBuffer.ToBytes();

            ByteBuffer readerByteBuffer = ByteBuffer.ValueOf();
            readerByteBuffer.WriteBytes(bytes);
            char readValue = readerByteBuffer.ReadChar();
            AssertEquals(value, readValue);
        }

        public void stringTest()
        {
            string value = "aaa";
            ByteBuffer writerByteBuffer = ByteBuffer.ValueOf();
            writerByteBuffer.WriteString(value);
            byte[] bytes = writerByteBuffer.ToBytes();

            ByteBuffer readerByteBuffer = ByteBuffer.ValueOf();
            readerByteBuffer.WriteBytes(bytes);
            string readValue = readerByteBuffer.ReadString();
            AssertEquals(value, readValue);
        }

        public static void AssertEquals(object a, object b)
        {
            if (a.Equals(b))
            {
                return;
            }

            throw new Exception("a is not equals b");
        }

        public static void AssertEquals<T>(T[] a, T[] b)
        {
            if (a == b)
            {
                return;
            }


            if (a != null && b != null && a.Length == b.Length)
            {
                for (var i = 0; i < a.Length; i++)
                {
                    AssertEquals(a[i], b[i]);
                }

                return;
            }

            throw new Exception("a is not equals b");
        }
    }
}